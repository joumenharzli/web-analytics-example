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

package com.github.joumenharzli.analytics;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.joumenharzli.utils.CassandraUtils;

import static com.github.joumenharzli.Configuration.WEB_ANALYTICS_KEYSPACE;
import static com.github.joumenharzli.analytics.ProductAccessTopicConstants.ACCESS_TIMESTAMP_WITHOUT_HOURS_MINUTES_SECONDS;
import static com.github.joumenharzli.analytics.ProductAccessTopicConstants.PRODUCT_ID;

/**
 * Bolt for counting the product views by timestamp
 *
 * @author Joumen Harzli
 */
public class ProductViewsCountByTimestampBolt extends BaseBasicBolt {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsCountByTimestampBolt.class);

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    LOGGER.debug("Counting the total of the product views by timestamp");

    String productId = input.getString(input.fieldIndex(PRODUCT_ID));
    String daysMonthYearTimestamp = input.getString(input.fieldIndex(ACCESS_TIMESTAMP_WITHOUT_HOURS_MINUTES_SECONDS));

    CassandraUtils.execute(WEB_ANALYTICS_KEYSPACE,
        String.format("update products_views_by_timestamp set count=count+1 where productId='%s' and timestamp='%s'",
            productId, daysMonthYearTimestamp));

  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    /* not needed */
  }

}
