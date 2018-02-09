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

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.github.joumenharzli.shop.ShopApplication;
import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.domain.Shop;
import com.github.joumenharzli.shop.domain.builder.ProductBuilder;
import com.github.joumenharzli.shop.exception.ProductNotFoundException;
import com.github.joumenharzli.shop.repository.ProductRepository;
import com.github.joumenharzli.shop.repository.ShopRepository;

import static com.github.joumenharzli.shop.web.ShopResourceTest.createShop;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test for the REST resource {@link ProductResource}
 *
 * @author Joumen Harzli
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@AutoConfigureMockMvc
public class ProductResourceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ShopRepository shopRepository;

  @Autowired
  private ProductRepository productRepository;

  public static Product createProduct(Shop shop, ProductRepository productRepository) {
    //@formatter:off
    Product product = ProductBuilder.aProduct()
                                    .withName(RandomStringUtils.randomAlphanumeric(5))
                                    .withPrice(Float.valueOf(RandomStringUtils.randomNumeric(5)))
                                    .withQuantity(Long.valueOf(RandomStringUtils.randomNumeric(5)))
                                    .withShop(shop)
                                    .build();
    //@formatter:on

    return productRepository.saveAndFlush(product);
  }

  @Test
  public void shouldGetProductById() throws Exception {
    Shop shop = createShop(shopRepository);
    Product product = createProduct(shop, productRepository);

    mockMvc.perform(get("/api/v1/products/{id}", product.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(product.getId().toString()))
        .andExpect(jsonPath("$.name").value(product.getName()))
        .andExpect(jsonPath("$.price").value(product.getPrice().toString()))
        .andExpect(jsonPath("$.quantity").value(product.getQuantity().toString()));
  }

  @Test
  public void shouldNotGetProductById() throws Exception {
    mockMvc.perform(get("/api/v1/products/{id}", UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.code").value(ProductNotFoundException.ERROR_CODE));
  }


}
