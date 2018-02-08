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

import java.util.List;
import java.util.UUID;

import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.domain.Shop;

/**
 * ShopBuilder
 *
 * @author Joumen Harzli
 */
public final class ShopBuilder {
  private Shop shop;

  private ShopBuilder() {
    shop = new Shop();
  }

  public static ShopBuilder aShop() {
    return new ShopBuilder();
  }

  public ShopBuilder withId(UUID id) {
    shop.setId(id);
    return this;
  }

  public ShopBuilder withName(String name) {
    shop.setName(name);
    return this;
  }

  public ShopBuilder withProducts(List<Product> products) {
    shop.setProducts(products);
    return this;
  }

  public Shop build() {
    return shop;
  }
}
