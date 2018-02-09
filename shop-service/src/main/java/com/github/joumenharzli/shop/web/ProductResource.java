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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.service.ProductService;
import com.github.joumenharzli.shop.service.dto.ProductDTO;
import com.github.joumenharzli.shop.web.error.RestErrorDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST resource for the entity {@link Product}
 *
 * @author Joumen Harzli
 */
@RestController
@RequestMapping("/api/v1")
public class ProductResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

  private final ProductService productService;

  public ProductResource(ProductService productService) {
    this.productService = productService;
  }

  /**
   * GET  /products/:id get the product having the specified id.
   *
   * @return the ResponseEntity with status 200 (OK) and the product having the specified id
   * or with status 404 (Not Found) if the shop was not found
   */
  @ApiOperation(notes = "Returns the found product having the specified id.",
      value = "Get the product having the specified id",
      nickname = "getOneById")
  @ApiResponses({
      @ApiResponse(code = 404, message = "Product not found", response = RestErrorDto.class),
  })
  @Timed
  @GetMapping("/products/{id}")
  public ProductDTO getOneById(@PathVariable("id") String productId) {
    LOGGER.debug("REST request to get the product having the id {}", productId);
    return productService.findOneById(productId);
  }

}
