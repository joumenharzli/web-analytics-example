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

package com.github.joumenharzli;

import org.apache.storm.StormSubmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.joumenharzli.Configuration.WEB_ANALYTICS_TOPOLOGY_NAME;
import static com.github.joumenharzli.analytics.WebAnalyticsTopology.createTopology;
import static com.github.joumenharzli.analytics.WebAnalyticsTopology.createTopologyConfiguration;

/**
 * Entry point where we submit the topology
 *
 * @author Joumen Harzli
 */
public class App {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  /**
   * Creation, Configuration and Submission of the topology
   *
   * @param args currently no args are needed
   */
  public static void main(String[] args) {
    LOGGER.info("Submitting {} topology to storm", WEB_ANALYTICS_TOPOLOGY_NAME);

    try {

      StormSubmitter.submitTopology(
          WEB_ANALYTICS_TOPOLOGY_NAME,
          createTopologyConfiguration(),
          createTopology());

    } catch (Exception e) {
      LOGGER.error("Unable to submit {} topology", WEB_ANALYTICS_TOPOLOGY_NAME, e);
    }

  }

}
