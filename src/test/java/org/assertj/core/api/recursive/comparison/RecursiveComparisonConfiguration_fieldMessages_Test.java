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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDSoftAssertions.thenSoftly;
import static org.assertj.core.util.Lists.list;

class RecursiveComparisonConfiguration_fieldMessages_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setUp() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_register_custom_message_for_fields() {
    // GIVEN
    String message = "field message";
    String fieldLocation = "field_1";
    // WHEN
    recursiveComparisonConfiguration.registerErrorMessageForFields(message, fieldLocation);
    // THEN
    thenSoftly(softly -> {
      softly.then(recursiveComparisonConfiguration.hasCustomMessageForField(fieldLocation)).isTrue();
      softly.then(recursiveComparisonConfiguration.getMessageForField(fieldLocation)).isEqualTo(message);
    });
  }

  @Test
  void should_register_custom_message_with_arguments_for_field() {
    // GIVEN
    String message = "field message with arguments %s and %s";
    List<Object> args = list("one", "two");
    String fieldLocation = "field_1";
    // WHEN
    recursiveComparisonConfiguration.registerErrorMessageForFields(message, args, fieldLocation);
    // THEN
    thenSoftly(softly -> {
      softly.then(recursiveComparisonConfiguration.hasCustomMessageForField(fieldLocation)).isTrue();
      softly.then(recursiveComparisonConfiguration.getMessageForField(fieldLocation))
            .isEqualTo(format(message, args.toArray()));
    });
  }

  @Test
  void should_throw_NPE_if_arguments_list_is_null() {
    // GIVEN
    String message = "field message";
    String fieldLocation = "field_1";
    List<Object> args = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> recursiveComparisonConfiguration.registerErrorMessageForFields(message, args, fieldLocation));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class).hasMessage("Arguments list must not be null");
  }
}
