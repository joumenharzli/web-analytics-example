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

package com.github.joumenharzli;

/**
 * Global Configuration
 * <p>
 * TODO: Replace this with apache common configuration
 *
 * @author Joumen Harzli
 */
public final class Configuration {

  public static final boolean ENABLE_DEBUGGING = false;
  public static final String CASSANDRA_CLUSTER_ADDRESS = "localhost";
  public static final String KAFKA_ZOOKEEPER_HOST = "127.0.0.1:2181";

  public static final String WEB_ANALYTICS_TOPOLOGY_NAME = "web-analytics";
  public static final String WEB_ANALYTICS_KEYSPACE = "web_analytics";
  public static final String PRODUCT_ACCESS_TOPIC = "product-access-topic";
  public static final int WEB_ANALYTICS_WORKERS_COUNT = 2;

  private Configuration() {
  }

}
