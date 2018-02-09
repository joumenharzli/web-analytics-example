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

package com.github.joumenharzli.shop.repository;

import java.util.List;
import java.util.UUID;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.joumenharzli.shop.domain.Product;

/**
 * JPA Repository for the entity {@link Product}
 *
 * @author Joumen Harzli
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

  @Query("SELECT p.id AS id, p.name AS name from Product p where p.shop.id = :shopid")
  List<Tuple> findAllByShopIdProjectedInTuple(@Param("shopid") UUID shopId);
}
