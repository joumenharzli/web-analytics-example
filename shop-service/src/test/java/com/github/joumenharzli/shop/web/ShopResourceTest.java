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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.github.joumenharzli.shop.config.ApplicationProperties;
import com.github.joumenharzli.shop.config.WebSecurityConfiguration;
import com.github.joumenharzli.shop.service.ShopService;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test for the REST resource {@link ShopResource}
 *
 * @author Joumen Harzli
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ShopResource.class)
@Import({WebSecurityConfiguration.class, ApplicationProperties.class})
public class ShopResourceTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ShopService shopService;

  @Test
  public void shouldGetAllShops() throws Exception {
    mockMvc.perform(get("/api/v1/shops")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$", hasSize(0)));
  }
}
