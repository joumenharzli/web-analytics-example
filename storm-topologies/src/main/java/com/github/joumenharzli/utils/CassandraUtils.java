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

package com.github.joumenharzli.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import static com.github.joumenharzli.Configuration.CASSANDRA_CLUSTER_ADDRESS;

/**
 * Cassandra Utils
 *
 * @author Joumen Harzli
 */
public final class CassandraUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraUtils.class);

  private static Cluster cluster;
  private static Map<String, Session> sessions = new HashMap<>();

  private CassandraUtils() {
  }

  /**
   * Execute a query in the specified keyspace
   *
   * @param keySpace name of the keyspace
   * @param cql      query to execute
   * @return result of the query
   */
  public static ResultSet execute(String keySpace, String cql) {
    Validate.notBlank(keySpace, "Cassandra keyspace cannot be null/blank");
    Validate.notBlank(cql, "Cassandra query cannot be null/blank");

    LOGGER.debug("Executing query in the keyspace {}", keySpace);
    LOGGER.trace("Executing query in the cluster {} in the keyspace {}: {}", CASSANDRA_CLUSTER_ADDRESS, keySpace, cql);

    return getSession(keySpace).execute(cql);
  }

  /**
   * Get or create session for the provided keyspace
   *
   * @param keySpace name of the keyspace
   * @return instance of the session
   */
  private static Session getSession(String keySpace) {
    if (sessions.containsKey(keySpace)) {

      LOGGER.debug("Using a cached session for the keyspace {}", keySpace);
      return sessions.get(keySpace);

    } else {

      LOGGER.debug("No cached session was found for the keyspace {}", keySpace);
      return createSession(keySpace);

    }
  }

  /**
   * Open a new cassandra session and cache it
   *
   * @param keySpace keyspace where the queries of session will be created
   * @return the created session
   */
  private static synchronized Session createSession(String keySpace) {

    /* Can be created by the previous thread */
    if (sessions.containsKey(keySpace) && !getCluster().isClosed()) {
      LOGGER.debug("Using the cached session for the keyspace {} created by the previous thread", keySpace);
      return sessions.get(keySpace);
    }

    LOGGER.debug("Create and cache sessions for the keyspace {}", keySpace);
    Session session = getCluster().connect(keySpace);
    sessions.put(keySpace, session);
    return session;
  }

  /**
   * Connect to the cassandra cluster or return a connected instance
   *
   * @return instance of the connected cluster
   */
  private static synchronized Cluster getCluster() {
    if (cluster == null || cluster.isClosed()) {
      LOGGER.debug("No available connection to the cluster was found");
      return connectToCluster();
    }

    LOGGER.debug("Using the available connection to the cluster");
    return cluster;
  }

  /**
   * Connect to the cassandra cluster
   *
   * @return instance of the connected cluster
   */
  private static Cluster connectToCluster() {
    LOGGER.debug("Connecting to the cluster with address {}", CASSANDRA_CLUSTER_ADDRESS);
    cluster = Cluster.builder().addContactPoint(CASSANDRA_CLUSTER_ADDRESS).build();
    return cluster;
  }

}
