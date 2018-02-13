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

package com.github.joumenharzli.analytics;

/**
 * Product Access Topic Constants
 *
 * @author Joumen Harzli
 */
public final class ProductAccessTopicConstants {

  public static final String USER_ID = "userId";
  public static final String PRODUCT_ID = "productId";
  public static final String ACCESS_TIMESTAMP = "accessTimestamp";
  public static final String ACCESS_TIMESTAMP_WITHOUT_HOURS_MINUTES_SECONDS = "accessTimestampWithoutHoursMinutesSeconds";

  private ProductAccessTopicConstants(){}
}
