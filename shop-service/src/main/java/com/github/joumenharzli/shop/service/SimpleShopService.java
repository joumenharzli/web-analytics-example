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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.joumenharzli.shop.repository.ShopRepository;
import com.github.joumenharzli.shop.service.dto.ShopDTO;
import com.github.joumenharzli.shop.service.mapper.ShopMapper;

/**
 * Simple Implementation of the service {@link ShopService}
 *
 * @author Joumen Harzli
 */
@Service
@Transactional
public class SimpleShopService implements ShopService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleShopService.class);

  private final ShopRepository shopRepository;
  private final ShopMapper shopMapper;

  public SimpleShopService(ShopRepository shopRepository, ShopMapper shopMapper) {
    this.shopRepository = shopRepository;
    this.shopMapper = shopMapper;
  }

  /**
   * Find all the shops without their products
   *
   * @return list of DTOs of the shops
   */
  @Override
  public List<ShopDTO> findAll() {
    LOGGER.debug("Request to get all the shops without their products");
    return shopMapper.toDtos(shopRepository.findAll());
  }
}
