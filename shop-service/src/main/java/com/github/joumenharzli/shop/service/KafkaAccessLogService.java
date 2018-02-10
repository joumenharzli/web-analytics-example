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

package com.github.joumenharzli.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.joumenharzli.shop.data.ProductAccessLog;
import com.github.joumenharzli.shop.security.UserDetails;
import com.github.joumenharzli.shop.service.dto.ProductDTO;

/**
 * KafkaAccessLogService
 *
 * @author Joumen Harzli
 */
@Service
public class KafkaAccessLogService implements AccessLogService {

  public static String PRODUCT_ACCESS_LOG_TOPIC = "product-access-topic";

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAccessLogService.class);

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaAccessLogService(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  /**
   * Log the access to the product
   *
   * @param userDetails user that accessed the product
   * @param product     the product that have been retrieved
   * @return the payload created
   */
  @Override
  public ProductAccessLog logProductAccess(Object userDetails, ProductDTO product) {
    Assert.notNull(product, "Product cannot be null");

    String userId = "anonymous";
    /* if it's not anonymous user */
    if (userDetails != null && userDetails instanceof UserDetails) {
      userId = ((UserDetails) userDetails).getId();
    }
    String productId = product.getId();

    Assert.hasText(userId, "User id cannot be null/blank");
    Assert.hasText(productId, "Product id cannot be null/blank");

    LOGGER.debug("Request to log the access of the user with id {} to the product with id {} to the topic {}",
        userId, productId, PRODUCT_ACCESS_LOG_TOPIC);

    ProductAccessLog productAccessLog = new ProductAccessLog(userId, productId);
    kafkaTemplate.send(PRODUCT_ACCESS_LOG_TOPIC, productAccessLog);

    return productAccessLog;
  }

}
