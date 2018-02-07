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

package com.github.joumenharzli.shop.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

/**
 * Cache Configuration
 *
 * @author Joumen Harzli
 */
@Configuration
@ConditionalOnProperty(prefix = "application.cache", value = "enabled")
@EnableCaching
public class CacheConfiguration {

  private final ApplicationProperties applicationProperties;

  public CacheConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public Config hazelCastConfiguration() {
    Config config = new Config()
        .setInstanceName(applicationProperties.getName())
        .addMapConfig(hazelCastDefaultMapConfiguration())
        .addMapConfig(hazelCastDomainMapConfiguration());

    configureAutoDiscovery(config);
    return config;
  }

  private MapConfig hazelCastDefaultMapConfiguration() {
    return new MapConfig()
        .setName("default")
        .setBackupCount(applicationProperties.getCache().getBackupCount())
        .setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE))
        .setEvictionPolicy(EvictionPolicy.LRU)
        .setTimeToLiveSeconds(applicationProperties.getCache().getTimeToLiveSeconds());
  }

  private MapConfig hazelCastDomainMapConfiguration() {
    return new MapConfig()
        .setName("com.github.joumenharzli.shop.domain.*")
        .setBackupCount(applicationProperties.getCache().getBackupCount())
        .setTimeToLiveSeconds(applicationProperties.getCache().getTimeToLiveSeconds());
  }

  private void configureAutoDiscovery(Config config) {
    final boolean enableAutoDiscovery = applicationProperties.getCache().isAutoDiscovery();
    config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(enableAutoDiscovery);
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(enableAutoDiscovery);
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(enableAutoDiscovery);
  }

}
