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

package com.github.joumenharzli.shop.web;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joumenharzli.shop.ShopApplication;
import com.github.joumenharzli.shop.data.ProductAccessLog;
import com.github.joumenharzli.shop.service.AccessLogService;
import com.github.joumenharzli.shop.service.KafkaAccessLogService;
import com.github.joumenharzli.shop.service.dto.ProductDTO;
import com.github.joumenharzli.shop.service.dto.builder.ProductDTOBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Access Log Service Test
 *
 * @author Joumen Harzli
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class AccessLogServiceTest {

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, KafkaAccessLogService.PRODUCT_ACCESS_LOG_TOPIC);

  private KafkaMessageListenerContainer<String, Object> container;

  private BlockingQueue<ConsumerRecord<String, Object>> records;

  @Autowired
  private AccessLogService accessLogService;

  @Before
  public void setUp() throws Exception {

    // set up the Kafka consumer properties
    Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("consumerGroup", "false", embeddedKafka);

    DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties);

    ContainerProperties containerProperties = new ContainerProperties(KafkaAccessLogService.PRODUCT_ACCESS_LOG_TOPIC);

    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();

    container.setupMessageListener((MessageListener<String, Object>) record -> records.add(record));

    // start the container and underlying message listener
    container.start();

    // wait until the container has the required number of assigned partitions
    ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
  }

  @After
  public void tearDown() {
    // stop the container
    container.stop();
  }

  @Test
  @WithAnonymousUser
  public void testSend() throws InterruptedException, JsonProcessingException {
    // send the message
    ProductDTO productDTO = ProductDTOBuilder.aProductDTO().withId(UUID.randomUUID().toString()).build();

    Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    ProductAccessLog payload = accessLogService.logProductAccess(user, productDTO);

    // check that the message was received
    ConsumerRecord<String, Object> received = records.poll(10, TimeUnit.SECONDS);

    ObjectMapper objectMapper = new ObjectMapper();
    assertEquals(received.value(), objectMapper.writeValueAsString(payload));
  }

}
