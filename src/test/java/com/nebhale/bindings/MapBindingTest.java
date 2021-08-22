/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.bindings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Map Binding")
final class MapBindingTest {

    private final MapBinding binding = new MapBinding("test-name",
        new FluentMap()
            .withEntry("provider", "test-provider-1")
            .withEntry("test-secret-key", "test-secret-value\n")
            .withEntry("type", "test-type-1")
            .asBytes()
    );

    @Test
    @DisplayName("returns well-known content")
    void wellKnownContent() {
        assertThat(binding.getName()).isEqualTo("test-name");
        assertThat(binding.getProvider()).isEqualTo("test-provider-1");
        assertThat(binding.getType()).isEqualTo("test-type-1");
    }

    @Test
    @DisplayName("returns binding-specific content")
    void content() {
        assertThat(binding.get("test-secret-key")).isEqualTo("test-secret-value");
    }

    @Test
    @DisplayName("returns null for missing key")
    void missingKey() {
        assertThat(binding.get("test-missing-key")).isNull();
    }

    @Test
    @DisplayName("returns null for invalid key")
    void invalidKey() {
        assertThat(binding.get("test^invalid^key")).isNull();
    }

    @Test
    @DisplayName("returns bytes")
    void bytes() {
        assertThat(binding.getAsBytes("test-secret-key")).isEqualTo("test-secret-value\n".getBytes(StandardCharsets.UTF_8));
    }

}
