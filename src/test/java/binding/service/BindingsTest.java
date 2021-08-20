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

package binding.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jdk.jfr.Timestamp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Bindings")
final class BindingsTest {

    private final Path root = Paths.get("src/test/resources");

    @Test
    @DisplayName("wraps with cached binding")
    void cached() {
        Binding[] bindings = Bindings.cached(new Binding[]{
            new MapBinding("test-name-1", Collections.emptyMap())
        });

        for (Binding binding : bindings) {
            assertThat(binding).isInstanceOf(CacheBinding.class);
        }

    }

    @Nested
    @DisplayName("from")
    final class From {

        @Test
        @DisplayName("empty if path does not exist")
        void nonExistentDirectory() {
            assertThat(Bindings.from(root.resolve("non-existent"))).isEmpty();
        }

        @Test
        @DisplayName("empty if path is not a directory")
        void nonDirectory() throws IOException {
            assertThat(Bindings.from(Paths.get("additional-file"))).isEmpty();
        }

        @Test
        @DisplayName("populates content")
        void construct() {
            assertThat(Bindings.from(root)).hasSize(3);
        }

    }

    @Nested
    @DisplayName("from service binding root")
    final class FromServiceBindingRoot {

        @Test
        @DisplayName("populates content")
        void construct() {
            assertThat(Bindings.fromServiceBindingRoot()).hasSize(3);
        }

    }

    @Nested
    @DisplayName("with content")
    final class Content {

        private final Binding[] bindings = new Binding[]{
            new MapBinding("test-name-1",
                new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
            ),
            new MapBinding("test-name-2",
                new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
            ),
            new MapBinding("test-name-3",
                new FluentMap()
                    .withEntry("type", "test-type-3")
                    .asBytes()
            )
        };

        @Test
        @DisplayName("find")
        void find() {
            assertThat(Bindings.find(bindings, "test-name-1")).isNotNull();
        }

        @Test
        @DisplayName("filters bindings by type")
        void filterByType() {
            assertThat(Bindings.filter(bindings, "test-type-1")).hasSize(1);
        }

        @Test
        @DisplayName("filters bindings by type and provider")
        void filterByTypeAndProvider() {
            assertThat(Bindings.filter(bindings, "test-type-1", "test-provider-1")).hasSize(1);
        }

        @Test
        @DisplayName("filters bindings by provider")
        void filterByProvider() {
            assertThat(Bindings.filter(bindings, null, "test-provider-1")).hasSize(1);
        }

    }

}
