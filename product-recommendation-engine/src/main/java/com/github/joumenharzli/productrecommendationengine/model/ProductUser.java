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

package com.github.joumenharzli.productrecommendationengine.model;

import java.io.Serializable;

/**
 * Product User
 *
 * @author Joumen Harzli
 */
public class ProductUser implements Serializable {
  private String productId;
  private String userId;

  public ProductUser(String productId, String userId) {
    this.productId = productId;
    this.userId = userId;
  }

  public String getProductId() {
    return productId;
  }

  public String getUserId() {
    return userId;
  }
}
