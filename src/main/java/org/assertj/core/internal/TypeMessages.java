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
package org.assertj.core.internal;

import org.assertj.core.util.ClassNameComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.util.Strings.join;
import static org.assertj.core.util.introspection.ClassUtils.getRelevantClass;

/**
 * An internal holder of the custom message for type. It is used to store messages for registered classes.
 * When looking for a message for a given class the holder returns the most relevant comparator.
 */
public class TypeMessages {

  private static final Comparator<Class<?>> DEFAULT_CLASS_COMPARATOR = ClassNameComparator.INSTANCE;

  private final Map<Class<?>, String> typeMessages;

  public TypeMessages() {
    typeMessages = new TreeMap<>(DEFAULT_CLASS_COMPARATOR);
  }

  /**
   * This method returns the most relevant error message for the given class. The most relevant message is the
   * message which is registered for the class that is closest in the inheritance chain of the given {@code clazz}.
   * The order of checks is the following:
   * 1. If there is a registered message for {@code clazz} then this one is used
   * 2. We check if there is a registered message for a superclass of {@code clazz}
   * 3. We check if there is a registered message for an interface of {@code clazz}
   *
   * @param clazz the class for which to find an error message
   * @return the most relevant error message, or {@code null} if no message could be found
   */
  public String getMessageForType(Class<?> clazz) {
    Class<?> relevantType = getRelevantClass(clazz, typeMessages.keySet());
    return relevantType == null ? null : typeMessages.get(relevantType);
  }

  /**
   * Checks, whether an any custom error message is associated with the giving type.
   *
   * @param type the type for which to check a error message
   * @return is the giving type associated with any custom error message
   */
  public boolean hasMessageForType(Class<?> type) {
    return getMessageForType(type) != null;
  }

  /**
   * Puts the {@code message} for the given {@code clazz}.
   *
   * @param clazz the class for the error message
   * @param message the error message itself
   * @param <T> the type of the objects to associate with the message for
   */
  public <T> void registerMessage(Class<T> clazz, String message) {
    typeMessages.put(clazz, message);
  }

  /**
   * @return {@code true} is there are registered error messages, {@code false} otherwise
   */
  public boolean isEmpty() {
    return typeMessages.isEmpty();
  }

  public Stream<Map.Entry<Class<?>, String>> messageByTypes() {
    return typeMessages.entrySet().stream();
  }

  @Override
  public int hashCode() {
    return typeMessages.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TypeMessages && Objects.equals(typeMessages, ((TypeMessages) obj).typeMessages);
  }

  @Override
  public String toString() {
    List<String> registeredErrorMessages = new ArrayList<>();
    for (Map.Entry<Class<?>, String> entry : typeMessages.entrySet()) {
      registeredErrorMessages.add(formatRegisteredErrorMessage(entry));
    }
    return format("{%s}", join(registeredErrorMessages).with("%n"));
  }

  private String formatRegisteredErrorMessage(Map.Entry<Class<?>, String> fieldMessage) {
    return format("%s -> %s", fieldMessage.getKey(), fieldMessage.getValue());
  }
}
