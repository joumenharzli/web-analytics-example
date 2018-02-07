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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.github.joumenharzli.shop.domain.Shop;
import com.github.joumenharzli.shop.service.ShopService;
import com.github.joumenharzli.shop.service.dto.ShopDTO;

import io.swagger.annotations.ApiOperation;

/**
 * REST resource for the entity {@link Shop}
 *
 * @author Joumen Harzli
 */
@RestController
@RequestMapping("/api/v1/shops")
public class ShopResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShopResource.class);

  private final ShopService shopService;

  public ShopResource(ShopService shopService) {
    this.shopService = shopService;
  }

  /**
   * GET  /shops : get all the shops without their products.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of the shops without their products
   */
  @ApiOperation(notes = "Returns all the found shops without their products.",
      value = "Get all shops and questions without their products",
      nickname = "getAllShops")
  @Timed
  @GetMapping
  public List<ShopDTO> getAllShops() {
    LOGGER.debug("REST request to get all the shops without their products");
    return shopService.findAll();
  }

}
