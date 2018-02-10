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

import com.github.joumenharzli.shop.data.ProductAccessLog;
import com.github.joumenharzli.shop.service.dto.ProductDTO;

/**
 * Access Log Service
 *
 * @author Joumen Harzli
 */
public interface AccessLogService {

  /**
   * Log the access to the product
   *
   * @param userDetails user that accessed the product
   * @param product     the product that have been retrieved
   * @return the payload created
   */
  ProductAccessLog logProductAccess(Object userDetails, ProductDTO product);

}
