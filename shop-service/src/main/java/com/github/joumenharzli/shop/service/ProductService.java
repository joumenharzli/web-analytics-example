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

package com.github.joumenharzli.shop.service;

import java.util.List;

import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.exception.ShopNotFoundException;
import com.github.joumenharzli.shop.service.dto.ProductSmallDTO;

/**
 * Logic Service of the entity {@link Product}
 *
 * @author Joumen Harzli
 */
public interface ProductService {

  /**
   * Find all the products by the shop id
   *
   * @param shopId id of the shop
   * @return list of Small DTOs of the products
   * @throws ShopNotFoundException    if the shop was not found
   * @throws IllegalArgumentException if any given argument is invalid
   */
  List<ProductSmallDTO> findAllProductsByShopId(String shopId);

}
