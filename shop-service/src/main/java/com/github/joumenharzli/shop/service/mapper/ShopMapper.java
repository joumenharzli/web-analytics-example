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

package com.github.joumenharzli.shop.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.github.joumenharzli.shop.domain.Shop;
import com.github.joumenharzli.shop.service.dto.ShopDTO;

/**
 * ShopMapper
 *
 * @author Joumen Harzli
 */
@Component
@Mapper(componentModel = "spring")
public interface ShopMapper {

  ShopDTO toDto(Shop shop);

  List<ShopDTO> toDtos(List<Shop> shops);

}
