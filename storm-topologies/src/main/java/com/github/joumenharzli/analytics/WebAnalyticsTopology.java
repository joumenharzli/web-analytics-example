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

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.joumenharzli.Configuration.*;
import static com.github.joumenharzli.utils.KafkaSpoutUtils.createKafkaSpout;

/**
 * Topology for the web analytics
 *
 * @author Joumen Harzli
 */
public final class WebAnalyticsTopology {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebAnalyticsTopology.class);

  private WebAnalyticsTopology() {
  }

  /**
   * The received stream from kafka is parsed then processed in parallel
   *
   * @return a web analytics topology
   */
  public static StormTopology createTopology() {
    LOGGER.debug("Creating a new web analytics topology");

    TopologyBuilder topology = new TopologyBuilder();

    topology.setSpout("kafka_spout", createKafkaSpout(PRODUCT_ACCESS_TOPIC));
    topology.setBolt("parse_product_views", new ProductViewsParseBolt()).shuffleGrouping("kafka_spout");

    topology.setBolt("log_product_views", new ProductViewsLogBolt()).shuffleGrouping("parse_product_views");
    topology.setBolt("count_product_views", new ProductViewsCountBolt()).shuffleGrouping("parse_product_views");
    topology.setBolt("count_product_views_by_timestamp", new ProductViewsCountByTimestampBolt()).shuffleGrouping("parse_product_views");
    topology.setBolt("count_product_views_by_user", new ProductViewsCountByUserBolt()).shuffleGrouping("parse_product_views");

    return topology.createTopology();

  }

  /**
   * @return the configuration of the topology
   */
  public static Config createTopologyConfiguration() {
    Config config = new Config();
    config.setDebug(ENABLE_DEBUGGING);
    config.setMaxTaskParallelism(WEB_ANALYTICS_WORKERS_COUNT);
    return config;
  }


}
