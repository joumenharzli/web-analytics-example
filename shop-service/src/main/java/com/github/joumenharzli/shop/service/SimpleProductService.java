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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.joumenharzli.shop.domain.Product;
import com.github.joumenharzli.shop.domain.Shop;
import com.github.joumenharzli.shop.domain.builder.ProductBuilder;
import com.github.joumenharzli.shop.domain.builder.ShopBuilder;
import com.github.joumenharzli.shop.exception.ProductNotFoundException;
import com.github.joumenharzli.shop.exception.ShopNotFoundException;
import com.github.joumenharzli.shop.repository.CustomShopRepository;
import com.github.joumenharzli.shop.repository.ProductRepository;
import com.github.joumenharzli.shop.repository.ShopRepository;
import com.github.joumenharzli.shop.service.dto.ProductDTO;
import com.github.joumenharzli.shop.service.dto.ProductSmallDTO;
import com.github.joumenharzli.shop.service.dto.builder.ProductSmallDTOBuilder;
import com.github.joumenharzli.shop.service.mapper.ProductMapper;

/**
 * Simple Implementation of the service {@link ProductService}
 *
 * @author Joumen Harzli
 */
@Service
@Transactional
public class SimpleProductService implements ProductService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProductService.class);

  private final ShopRepository shopRepository;
  private final ProductRepository productRepository;
  private final CustomShopRepository customShopRepository;
  private final ProductMapper productMapper;

  public SimpleProductService(ShopRepository shopRepository, ProductRepository productRepository,
                              CustomShopRepository customShopRepository,
                              ProductMapper productMapper) {
    this.shopRepository = shopRepository;
    this.productRepository = productRepository;
    this.customShopRepository = customShopRepository;
    this.productMapper = productMapper;
  }

  @PostConstruct
  public void init() {
    Shop shopA = ShopBuilder.aShop().withName("Shop A").build();
    Shop shopB = ShopBuilder.aShop().withName("Shop B").build();

    shopA = shopRepository.saveAndFlush(shopA);
    shopB = shopRepository.saveAndFlush(shopB);

    Product productA = ProductBuilder.aProduct().withName("Product A").withPrice(1F).withQuantity(5L).withShop(shopA).build();
    Product productB = ProductBuilder.aProduct().withName("Product B").withPrice(2F).withQuantity(5L).withShop(shopA).build();
    Product productC = ProductBuilder.aProduct().withName("Product C").withPrice(3F).withQuantity(5L).withShop(shopB).build();

    productRepository.save(Arrays.asList(productA, productB, productC));
  }

  /**
   * Find all the products by the shop id
   *
   * @param shopId id of the shop
   * @return list of Small DTOs of the products
   * @throws ShopNotFoundException    if the shop was not found
   * @throws IllegalArgumentException if any given argument is invalid
   */
  @Override
  @Transactional(readOnly = true)
  public List<ProductSmallDTO> findAllProductsByShopId(String shopId) {
    LOGGER.debug("Request to get all the products that belongs to the shop having the id {}", shopId);

    Assert.hasText(shopId, "id of the shop cannot be null/blank");
    assertShopExists(shopId);

    List<Tuple> products = productRepository.findAllByShopIdProjectedInTuple(UUID.fromString(shopId));
    return products.stream().map(this::tupleToProductSmallDTO).collect(Collectors.toList());
  }

  /**
   * Find product by his id
   *
   * @param productId id of the product
   * @return the found product
   * @throws ProductNotFoundException if the product was not found
   * @throws IllegalArgumentException if any given argument is invalid
   */
  @Override
  @Transactional(readOnly = true)
  public ProductDTO findOneById(String productId) {
    LOGGER.debug("Request to get the product having the id {}", productId);

    Assert.hasText(productId, "id of the product cannot be null/blank");

    Product product = productRepository.findOne(UUID.fromString(productId));
    if (product == null) {
      throw new ProductNotFoundException("The product with id " + productId + " was not found");
    }

    return productMapper.toDto(product);
  }

  /**
   * Check that the shop exists
   *
   * @param shopId id of the shop
   * @throws ShopNotFoundException if the shop was not found
   */
  private void assertShopExists(String shopId) {
    if (!customShopRepository.exists(UUID.fromString(shopId))) {
      throw new ShopNotFoundException(String.format("Shop with id %s was not found", shopId));
    }
  }

  private ProductSmallDTO tupleToProductSmallDTO(Tuple tuple) {
    //@formatter:off
    return ProductSmallDTOBuilder.aProductSmallDTO()
                                 .withId(tuple.get("id").toString())
                                 .withName(tuple.get("name").toString())
                                 .build();
    //@formatter:on
  }

}
