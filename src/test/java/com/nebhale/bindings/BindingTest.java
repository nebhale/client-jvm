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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

final class BindingTest {
    @Nested
    final class Get {
        @Test
        void missing() {
            Binding b = new MapBinding("test-name", Collections.emptyMap());
            assertThat(b.get("test-missing-key")).isNull();
        }

        @Test
        void valid() {
            Binding b = new MapBinding("test-name", new FluentMap()
                .withEntry("test-key", "test-value")
                .asBytes());

            assertThat(b.get("test-key")).isEqualTo("test-value");
        }
    }

    @Nested
    final class GetProvider {
        @Test
        void missing() {
            Binding b = new MapBinding("test-name", Collections.emptyMap());
            assertThat(b.getProvider()).isNull();
        }

        @Test
        void valid() {
            Binding b = new MapBinding("test-name", new FluentMap()
                .withEntry("provider", "test-provider")
                .asBytes());

            assertThat(b.getProvider()).isEqualTo("test-provider");
        }
    }

    @Nested
    final class GetType {
        @Test
        void invalid() {
            Binding b = new MapBinding("test-name", Collections.emptyMap());
            assertThatIllegalStateException().isThrownBy(b::getType);
        }

        @Test
        void valid() {
            Binding b = new MapBinding("test-name", new FluentMap()
                .withEntry("type", "test-type")
                .asBytes());

            assertThat(b.getType()).isEqualTo("test-type");
        }
    }
}
