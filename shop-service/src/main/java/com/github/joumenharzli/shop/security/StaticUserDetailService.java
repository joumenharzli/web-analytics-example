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

package com.github.joumenharzli.shop.security;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * A static implementation of {@link UserDetailsService}
 *
 * @author Joumen Harzli
 */
@Component
public class StaticUserDetailService implements UserDetailsService {

  private Map<String, UserDetails> userRegistry;

  @PostConstruct
  public void init() {
    userRegistry = new ConcurrentHashMap<>();
    addUser("user1");
    addUser("user2");
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserDetails user = userRegistry.get(username);
    if (user == null) {
      throw new UsernameNotFoundException("The user with name " + username + " was not found");
    }
    return user;
  }

  private void addUser(String username) {
    userRegistry.put(username,
        new UserDetails(UUID.randomUUID().toString(), username, username,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
  }
}
