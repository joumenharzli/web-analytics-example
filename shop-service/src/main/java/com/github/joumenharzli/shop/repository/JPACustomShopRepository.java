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

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.github.joumenharzli.shop.domain.Shop;

/**
 * JPA Custom Repository for the entity {@link Shop}
 *
 * @author Joumen Harzli
 */
@Repository
public class JPACustomShopRepository implements CustomShopRepository {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Check if the shop entity exists
   *
   * @param id entity id
   * @return true if it exists and false if not
   */
  @Override
  public boolean exists(UUID id) {
    return !entityManager.createQuery("select 1 from Shop s where s.id = :id")
        .setParameter("id", id)
        .getResultList().isEmpty();
  }

}
