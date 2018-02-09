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
import com.github.joumenharzli.shop.domain.builder.ShopBuilder;
import com.github.joumenharzli.shop.exception.ShopNotFoundException;
import com.github.joumenharzli.shop.repository.ProductRepository;
import com.github.joumenharzli.shop.repository.ShopRepository;

import static com.github.joumenharzli.shop.web.ProductResourceTest.createProduct;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test for the REST resource {@link ShopResource}
 *
 * @author Joumen Harzli
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@AutoConfigureMockMvc
public class ShopResourceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ShopRepository shopRepository;

  @Autowired
  private ProductRepository productRepository;

  public static Shop createShop(ShopRepository shopRepository) {
    //@formatter:off
    Shop shop = ShopBuilder.aShop()
                           .withName(RandomStringUtils.randomAlphanumeric(5))
                           .build();
    //@formatter:on

    return shopRepository.saveAndFlush(shop);
  }

  @Test
  public void shouldGetAllShops() throws Exception {
    Shop shop = createShop(shopRepository);

    mockMvc.perform(get("/api/v1/shops")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.[*].id").value(hasItem(shop.getId().toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(shop.getName())));
  }

  @Test
  public void shouldGetAllProductsByShopId() throws Exception {
    Shop shop = createShop(shopRepository);
    Product product = createProduct(shop, productRepository);

    mockMvc.perform(get("/api/v1/shops/{id}/products", shop.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(product.getName())));
  }

  @Test
  public void shouldNotGetAllProductsByShopId() throws Exception {
    mockMvc.perform(get("/api/v1/shops/{id}/products", UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.code").value(ShopNotFoundException.ERROR_CODE));
  }

}
