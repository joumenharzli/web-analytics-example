/*
 * Copyright (C) 2018 Joumen Harzli
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.github.joumenharzli.productrecommendationengine;

import java.time.Instant;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.github.joumenharzli.productrecommendationengine.model.ProductUser;
import com.github.joumenharzli.productrecommendationengine.model.ProductUserRatingPrediction;
import com.github.joumenharzli.productrecommendationengine.model.ProductUserViews;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import scala.Tuple2;

/**
 * Recommendation Engine
 * <p>
 * TODO: Use Common Configuration and add JavaDoc
 *
 * @author Joumen Harzli
 */
public class App {

  private static final String APP_NAME = "Product Recommendation Engine";
  private static final String SPARK_MASTER = "local[*]";

  private static final String CASSANDRA_CLUSTER_ADDRESS = "localhost";
  private static final String CASSANDRA_KEYSPACE = "web_analytics";
  private static final String CASSANDRA_COLUMN_FAMILY_FOR_READ = "products_views_by_user";
  private static final String CASSANDRA_COLUMN_FAMILY_FOR_WRITE = "product_recommendations";
  private static final String USER_ID_COLUMN_NAME = "userid";
  private static final String PRODUCT_ID_COLUMN_NAME = "productid";
  private static final String COUNT_COLUMN_NAME = "count";

  private static final String USER_ID_INDEX_NAME = "userIdNumber";
  private static final String PRODUCT_ID_INDEX_NAME = "productIdNumber";

  public static void main(String[] args) {
    SparkConf sparkConf = getSparkConfiguration();

    try (JavaSparkContext sc = new JavaSparkContext(sparkConf)) {
      SQLContext sqlContext = new SQLContext(sc);

      // Loading the data and transform it
      JavaRDD<ProductUserViews> productUserViewsJavaRDD = loadProductUserViewsFromDatabase(sc);
      Dataset<Row> dataFrame = sqlContext.createDataFrame(productUserViewsJavaRDD, ProductUserViews.class);
      Dataset<Row> indexedDataSet = createUserAndProductIdsIndexes(dataFrame);
      JavaRDD<Rating> ratings = createRatingData(indexedDataSet);

      // Train the model
      MatrixFactorizationModel model = trainModel(ratings);

      // Prepare the data for prediction
      JavaPairRDD<Integer, Integer> productUsersRdd = extractDataForPrediction(ratings);
      Dataset<Row> productIndexed = extractProductIdAndIndexDataset(indexedDataSet);
      Dataset<Row> userIndexed = extractUserIdAndIndexDataset(indexedDataSet);

      // Get the predictions and transform it
      JavaRDD<Rating> predictions = getPredictions(model, productUsersRdd);
      JavaRDD<ProductUserRatingPrediction> results = transformPredictions(productIndexed, userIndexed, predictions, sqlContext);

      // Save the predictions
      savePredictionsToDatabase(results);

    }

  }

  private static SparkConf getSparkConfiguration() {
    return new SparkConf()
        .setAppName(APP_NAME)
        .setMaster(SPARK_MASTER)
        .set("spark.cassandra.connection.host", CASSANDRA_CLUSTER_ADDRESS);
  }

  private static JavaRDD<ProductUserViews> loadProductUserViewsFromDatabase(JavaSparkContext sc) {
    return CassandraJavaUtil.javaFunctions(sc)
        .cassandraTable(CASSANDRA_KEYSPACE, CASSANDRA_COLUMN_FAMILY_FOR_READ)
        .select(PRODUCT_ID_COLUMN_NAME, USER_ID_COLUMN_NAME, COUNT_COLUMN_NAME)
        .keyBy(row -> new ProductUser(row.getString(PRODUCT_ID_COLUMN_NAME), row.getString(USER_ID_COLUMN_NAME)))
        .mapToPair(rowTuple2 -> new Tuple2<>(rowTuple2._1(), rowTuple2._2().getInt(COUNT_COLUMN_NAME)))
        .reduceByKey((v1, v2) -> v1 + v2)
        .map(fn -> new ProductUserViews(fn._1().getProductId(), fn._1().getUserId(), fn._2()));
  }

  private static Dataset<Row> createUserAndProductIdsIndexes(Dataset<Row> dataSet) {
    Dataset<Row> updatedDataSet = createUserIdIndexes(dataSet);

    return new StringIndexer()
        .setInputCol(PRODUCT_ID_COLUMN_NAME)
        .setOutputCol(PRODUCT_ID_INDEX_NAME)
        .fit(updatedDataSet)
        .transform(updatedDataSet);
  }

  private static JavaRDD<Rating> createRatingData(Dataset<Row> dataSet) {
    return dataSet.toJavaRDD()
        .map(row ->
            new Rating(((Double) row.getDouble(row.fieldIndex(USER_ID_INDEX_NAME))).intValue(),
                ((Double) row.getDouble(row.fieldIndex(PRODUCT_ID_INDEX_NAME))).intValue(),
                Double.valueOf((Integer) row.getInt(row.fieldIndex("totalViews")))));
  }

  private static MatrixFactorizationModel trainModel(JavaRDD<Rating> ratings) {
    int rank = 10;
    int numIterations = 10;
    return ALS.train(ratings.rdd(), rank, numIterations, 0.01);
  }

  private static JavaPairRDD<Integer, Integer> extractDataForPrediction(JavaRDD<Rating> ratingJavaRDD) {
    return ratingJavaRDD.cartesian(ratingJavaRDD)
        .mapToPair(value -> new Tuple2<>(value._1().product(), value._2().user()))
        .distinct();
  }

  private static Dataset<Row> extractProductIdAndIndexDataset(Dataset<Row> dataSet) {
    return dataSet.select(PRODUCT_ID_COLUMN_NAME, PRODUCT_ID_INDEX_NAME).distinct();
  }

  private static Dataset<Row> extractUserIdAndIndexDataset(Dataset<Row> dataSet) {
    return dataSet.select(USER_ID_COLUMN_NAME, USER_ID_INDEX_NAME).distinct();
  }

  private static JavaRDD<Rating> getPredictions(MatrixFactorizationModel model, JavaPairRDD<Integer, Integer> productUsersRdd) {
    return model.predict(productUsersRdd);
  }

  private static JavaRDD<ProductUserRatingPrediction> transformPredictions(Dataset<Row> productConverted,
                                                                           Dataset<Row> userConverted,
                                                                           JavaRDD<Rating> predictionJavaRDD,
                                                                           SQLContext sqlContext) {
    Dataset<Row> predictionsDataSet = sqlContext.createDataFrame(predictionJavaRDD, Rating.class);

    String predictionDate = Instant.now().toString();

    return predictionsDataSet

        .join(productConverted,
            productConverted.col(PRODUCT_ID_INDEX_NAME).equalTo(predictionsDataSet.col("product")), "left")

        .join(userConverted,
            userConverted.col(USER_ID_INDEX_NAME).equalTo(predictionsDataSet.col("user")), "left")

        .select(USER_ID_COLUMN_NAME, PRODUCT_ID_COLUMN_NAME, "rating")

        .toJavaRDD().map(v1 -> new ProductUserRatingPrediction(
            v1.getString(v1.fieldIndex(PRODUCT_ID_COLUMN_NAME)),
            v1.getString(v1.fieldIndex(USER_ID_COLUMN_NAME)),
            v1.getDouble(v1.fieldIndex("rating")),
            predictionDate));
  }

  private static void savePredictionsToDatabase(JavaRDD<ProductUserRatingPrediction> results) {
    CassandraJavaUtil
        .javaFunctions(results)
        .writerBuilder(CASSANDRA_KEYSPACE, CASSANDRA_COLUMN_FAMILY_FOR_WRITE, mapToRow(ProductUserRatingPrediction.class))
        .saveToCassandra();
  }

  private static Dataset<Row> createUserIdIndexes(Dataset<Row> dataSet) {
    return new StringIndexer()
        .setInputCol(USER_ID_COLUMN_NAME)
        .setOutputCol(USER_ID_INDEX_NAME)
        .fit(dataSet)
        .transform(dataSet);
  }


}
