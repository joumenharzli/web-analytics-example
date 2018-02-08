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

package com.github.joumenharzli.shop.domain.builder;

import java.util.UUID;

import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.domain.Shop;

/**
 * ProductBuilder
 *
 * @author Joumen Harzli
 */
public final class ProductBuilder {
  private Product product;

  private ProductBuilder() {
    product = new Product();
  }

  public static ProductBuilder aProduct() {
    return new ProductBuilder();
  }

  public ProductBuilder withId(UUID id) {
    product.setId(id);
    return this;
  }

  public ProductBuilder withName(String name) {
    product.setName(name);
    return this;
  }

  public ProductBuilder withPrice(Float price) {
    product.setPrice(price);
    return this;
  }

  public ProductBuilder withQuantity(Long quantity) {
    product.setQuantity(quantity);
    return this;
  }

  public ProductBuilder withShop(Shop shop) {
    product.setShop(shop);
    return this;
  }

  public Product build() {
    return product;
  }
}
