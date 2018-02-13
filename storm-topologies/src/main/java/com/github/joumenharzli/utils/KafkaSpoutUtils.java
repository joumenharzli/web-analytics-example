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

package com.github.joumenharzli.utils;

import org.apache.commons.lang3.Validate;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.joumenharzli.Configuration.KAFKA_ZOOKEEPER_HOST;

/**
 * Kafka Spout Utils
 *
 * @author Joumen Harzli
 */
public final class KafkaSpoutUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSpoutUtils.class);

  private KafkaSpoutUtils() {
  }

  /**
   * Create a kafka spout
   *
   * @param topicName name of the topic
   * @return the created spout
   */
  public static KafkaSpout createKafkaSpout(String topicName) {
    Validate.notBlank(topicName, "The name of the kafka topic cannot be null/blank");

    LOGGER.debug("Creating a new kafka spout that will subscribe to the topic {}", topicName);

    SpoutConfig kafkaConfig = new SpoutConfig(
        new ZkHosts(KAFKA_ZOOKEEPER_HOST), topicName, "/kafka", "kafka-spout");

    kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

    return new KafkaSpout(kafkaConfig);
  }

}


