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

package com.github.joumenharzli.shop.service.dto.builder;

import com.github.joumenharzli.shop.service.dto.ProductDTO;

/**
 * ProductDTOBuilder
 *
 * @author Joumen Harzli
 */
public final class ProductDTOBuilder {
  private ProductDTO productDTO;

  private ProductDTOBuilder() {
    productDTO = new ProductDTO();
  }

  public static ProductDTOBuilder aProductDTO() {
    return new ProductDTOBuilder();
  }

  public ProductDTOBuilder withId(String id) {
    productDTO.setId(id);
    return this;
  }

  public ProductDTOBuilder withName(String name) {
    productDTO.setName(name);
    return this;
  }

  public ProductDTOBuilder withPrice(Float price) {
    productDTO.setPrice(price);
    return this;
  }

  public ProductDTOBuilder withQuantity(Long quantity) {
    productDTO.setQuantity(quantity);
    return this;
  }

  public ProductDTO build() {
    return productDTO;
  }
}
