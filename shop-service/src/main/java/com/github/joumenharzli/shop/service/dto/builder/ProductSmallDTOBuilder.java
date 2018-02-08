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

import com.github.joumenharzli.shop.service.dto.ProductSmallDTO;

/**
 * ProductSmallDTOBuilder
 *
 * @author Joumen Harzli
 */
public final class ProductSmallDTOBuilder {
  private ProductSmallDTO productSmallDTO;

  private ProductSmallDTOBuilder() {
    productSmallDTO = new ProductSmallDTO();
  }

  public static ProductSmallDTOBuilder aProductSmallDTO() {
    return new ProductSmallDTOBuilder();
  }

  public ProductSmallDTOBuilder withId(String id) {
    productSmallDTO.setId(id);
    return this;
  }

  public ProductSmallDTOBuilder withName(String name) {
    productSmallDTO.setName(name);
    return this;
  }

  public ProductSmallDTO build() {
    return productSmallDTO;
  }
}
