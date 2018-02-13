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

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.joumenharzli.analytics.ProductAccessTopicConstants.*;

/**
 * Bolt for parsing the received product views from kafka
 *
 * @author Joumen Harzli
 */
public class ProductViewsParseBolt extends BaseBasicBolt {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductViewsParseBolt.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Parse the received {@code String} content from the kafka spout and emit the extracted
   * values
   */
  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    LOGGER.debug("Parsing the received content from Kafka");

    String jsonContent = input.getString(0);
    validateReceivedContent(jsonContent);

    try {

      Values values = parseReceivedContentValues(jsonContent);
      collector.emit(values);

    } catch (IOException e) {
      LOGGER.error("Unable to parse the content received from Kafka {}", e);
      throw new FailedException("Unable to parse the content received from Kafka", e);
    }

  }

  /**
   * Parse the received content
   *
   * @param jsonContent the received content from Kafka
   * @return the extracted values
   * @throws IOException if the provided cannot be parsed
   */
  private Values parseReceivedContentValues(String jsonContent) throws IOException {

    JsonNode node = objectMapper.readValue(jsonContent, JsonNode.class);

    String userId = node.get(USER_ID).asText();
    String productId = node.get(PRODUCT_ID).asText();
    String accessTimestamp = node.get(ACCESS_TIMESTAMP).asText();

    String daysMonthYearTimestamp = Instant.parse(accessTimestamp)
        .truncatedTo(ChronoUnit.DAYS)
        .toString();

    validateValue(USER_ID, userId);
    validateValue(PRODUCT_ID, productId);
    validateValue(ACCESS_TIMESTAMP, accessTimestamp);
    validateValue(ACCESS_TIMESTAMP_WITHOUT_HOURS_MINUTES_SECONDS, daysMonthYearTimestamp);

    Values values = new Values(userId, productId, accessTimestamp, daysMonthYearTimestamp);

    LOGGER.trace("The parsed values are: ", values);
    return values;
  }

  /**
   * The user id, the product id, and the access timestamp will be emitted after the parsing
   */
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields(USER_ID, PRODUCT_ID, ACCESS_TIMESTAMP, ACCESS_TIMESTAMP_WITHOUT_HOURS_MINUTES_SECONDS));
  }

  /**
   * Validate that the received content is valid
   *
   * @param jsonContent the received content from Kafka
   */
  private void validateReceivedContent(String jsonContent) {
    LOGGER.trace("The received content is {}", jsonContent);

    if (StringUtils.isEmpty(jsonContent)) {
      LOGGER.error("Blank or null content received from Kafka");
      throw new FailedException("Blank or null content received from Kafka");
    }
  }

  /**
   * Validate that the value is not null or blank
   *
   * @param valueName    name of the value
   * @param valueContent content of the value
   * @throws FailedException if the value is not valid
   */
  private void validateValue(String valueName, String valueContent) {

    if (StringUtils.isEmpty(valueContent)) {
      LOGGER.error("{} is Blank or null", valueName);
      throw new FailedException(String.format("%s is Blank or null", valueName));
    }

  }

}
