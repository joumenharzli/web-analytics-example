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

package com.github.joumenharzli.shop.exception;

/**
 * A generic exception for modeling the entity not found exception
 *
 * @author Joumen Harzli
 */
public class EntityNotFoundException extends RuntimeException {

  /* The default error code for representing an entity that was not found */
  public static final String ERROR_CODE = "error.entityNotFound";

  /**
   * Constructs a new runtime exception with the specified detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public EntityNotFoundException(String message) {
    super(message);
  }

  /**
   * @return the error code of the exception
   */
  public String getErrorCode() {
    return ERROR_CODE;
  }

}
