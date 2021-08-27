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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class CacheBindingTest {
    @Test
    void missing() {
        StubBinding s = new StubBinding();
        CacheBinding b = new CacheBinding(s);

        assertThat(b.get("test-unknown-key")).isNull();
        assertThat(b.get("test-unknown-key")).isNull();
        assertThat(s.getAsBytesCount).isEqualTo(2);
    }

    @Test
    void valid() {
        StubBinding s = new StubBinding();
        CacheBinding b = new CacheBinding(s);

        assertThat(b.get("test-secret-key")).isNotNull();
        assertThat(b.get("test-secret-key")).isNotNull();
        assertThat(s.getAsBytesCount).isEqualTo(1);
    }

    @Test
    void getName() {
        StubBinding s = new StubBinding();
        CacheBinding b = new CacheBinding(s);

        assertThat(b.getName()).isNotNull();
        assertThat(b.getName()).isNotNull();
        assertThat(s.getNameCount).isEqualTo(2);
    }

    private static final class StubBinding implements Binding {
        private int getAsBytesCount = 0;
        private int getNameCount = 0;

        @Nullable
        @Override
        public byte[] getAsBytes(@NotNull String key) {
            getAsBytesCount++;

            if ("test-secret-key".equals(key)) {
                return new byte[0];
            }

            return null;
        }

        @NotNull
        @Override
        public String getName() {
            getNameCount++;
            return "test-name";
        }

    }
}
