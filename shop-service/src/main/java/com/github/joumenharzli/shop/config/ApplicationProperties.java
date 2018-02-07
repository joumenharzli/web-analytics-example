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

import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties relative to the application
 *
 * @author Joumen Harzli
 */
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  private String name;
  private String version;
  private final SwaggerProperties swagger = new SwaggerProperties();
  private final CorsProperties cors = new CorsProperties();
  private final LocalizationProperties localization = new LocalizationProperties();
  private final CacheProperties cache = new CacheProperties();

  public static class SwaggerProperties {

    private boolean enabled;
    private String title;
    private String description;
    private String repositoryUrl;
    private String licenseName;
    private String licenseUrl;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getRepositoryUrl() {
      return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
      this.repositoryUrl = repositoryUrl;
    }

    public String getLicenseName() {
      return licenseName;
    }

    public void setLicenseName(String licenseName) {
      this.licenseName = licenseName;
    }

    public String getLicenseUrl() {
      return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
      this.licenseUrl = licenseUrl;
    }
  }

  public static class CorsProperties {

    private boolean enabled;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }

  public static class LocalizationProperties {

    private boolean enabled;
    private Locale defaultLocale = Locale.US;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public Locale getDefaultLocale() {
      return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {
      this.defaultLocale = defaultLocale;
    }
  }

  public static class CacheProperties {

    private boolean enabled;
    private boolean autoDiscovery;
    private int backupCount;
    private int timeToLiveSeconds;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public boolean isAutoDiscovery() {
      return autoDiscovery;
    }

    public void setAutoDiscovery(boolean autoDiscovery) {
      this.autoDiscovery = autoDiscovery;
    }

    public int getBackupCount() {
      return backupCount;
    }

    public void setBackupCount(int backupCount) {
      this.backupCount = backupCount;
    }

    public int getTimeToLiveSeconds() {
      return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
      this.timeToLiveSeconds = timeToLiveSeconds;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public SwaggerProperties getSwagger() {
    return swagger;
  }

  public CorsProperties getCors() {
    return cors;
  }

  public LocalizationProperties getLocalization() {
    return localization;
  }

  public CacheProperties getCache() {
    return cache;
  }
}
