/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * An internal holder of the custom messages for fields described by their path without element index.
 */
public class FieldMessages {

  private final Map<String, String> fieldMessages;

  public FieldMessages() {
    fieldMessages = new TreeMap<>();
  }

  /**
   * Pairs the giving error {@code message} with the {@code fieldLocation}.
   *
   * @param fieldLocation the field location where to apply the giving error message
   * @param message the error message
   */
  public void registerMessage(String fieldLocation, String message) {
    fieldMessages.put(fieldLocation, message);
  }

  /**
   * @return {@code true} is there are registered messages, {@code false} otherwise
   */
  public boolean isEmpty() {
    return fieldMessages.isEmpty();
  }

  @Override
  public int hashCode() {
    return fieldMessages.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FieldMessages && Objects.equals(fieldMessages, ((FieldMessages) obj).fieldMessages);
  }

  @Override
  public String toString() {
    List<String> registeredErrorMessages = new ArrayList<>();
    for (Entry<String, String> entry : fieldMessages.entrySet()) {
      registeredErrorMessages.add(formatRegisteredErrorMessage(entry));
    }
    return format("{%s}", join(registeredErrorMessages).with("%n"));
  }

  private String formatRegisteredErrorMessage(Entry<String, String> fieldMessage) {
    return format("%s -> %s", fieldMessage.getKey(), fieldMessage.getValue());
  }

  public boolean hasMessageForField(String fieldLocation) {
    return fieldMessages.containsKey(fieldLocation);
  }

  public String getMessageForField(String fieldLocation) {
    return fieldMessages.get(fieldLocation);
  }

  public Stream<Entry<String, String>> messageByFields() {
    return fieldMessages.entrySet().stream();
  }
}
