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

package com.github.joumenharzli.shop.data;

import java.io.Serializable;
import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * ProductAccessLog
 *
 * @author Joumen Harzli
 */
public class ProductAccessLog implements Serializable {

  private String userId;

  private String productId;

  private Instant accessTimestamp;

  public ProductAccessLog(String userId, String productId) {
    this.userId = userId;
    this.productId = productId;
    this.accessTimestamp = Instant.now();
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Instant getAccessTimestamp() {
    return accessTimestamp;
  }

  public void setAccessTimestamp(Instant accessTimestamp) {
    this.accessTimestamp = accessTimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ProductAccessLog)) {
      return false;
    }

    ProductAccessLog that = (ProductAccessLog) o;

    return new EqualsBuilder()
        .append(userId, that.userId)
        .append(productId, that.productId)
        .append(accessTimestamp, that.accessTimestamp)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(userId)
        .append(productId)
        .append(accessTimestamp)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("userId", userId)
        .append("productId", productId)
        .append("accessTimestamp", accessTimestamp)
        .toString();
  }
}
