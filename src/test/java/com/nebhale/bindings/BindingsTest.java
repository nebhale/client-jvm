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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

final class BindingsTest {
    @Test
    void cached() {
        Binding[] bindings = Bindings.cached(new Binding[]{
            new MapBinding("test-name-1", Collections.emptyMap()),
            new MapBinding("test-name-2", Collections.emptyMap())
        });

        for (Binding binding : bindings) {
            assertThat(binding).isInstanceOf(CacheBinding.class);
        }
    }

    @Nested
    final class From {
        @Test
        void missing() {
            assertThat(Bindings.from(Paths.get("src/test/resources/missing"))).isEmpty();
        }

        @Test
        void file() {
            assertThat(Bindings.from(Paths.get("src/test/resources/additional-file"))).isEmpty();
        }

        @Test
        void valid() {
            assertThat(Bindings.from(Paths.get("src/test/resources"))).hasSize(3);
        }
    }

    @Nested
    final class FromServiceBindingRoot {
        @Test
        void unset() {
            assertThat(Bindings.fromServiceBindingRoot()).isEmpty();
        }

        @Test
        void set() throws Exception {
            String old = System.getenv("SERVICE_BINDING_ROOT");
            getModifiableEnvironment().put("SERVICE_BINDING_ROOT", "src/test/resources");

            try {
                assertThat(Bindings.fromServiceBindingRoot()).hasSize(3);
            } finally {
                if (old != null) {
                    getModifiableEnvironment().put("SERVICE_BINDING_ROOT", old);
                } else {
                    getModifiableEnvironment().remove("SERVICE_BINDING_ROOT");
                }
            }
        }

        @SuppressWarnings("unchecked")
        private Map<String, String> getModifiableEnvironment() throws Exception {
            Class<?> pe = Class.forName("java.lang.ProcessEnvironment");
            Method getenv = pe.getDeclaredMethod("getenv");
            getenv.setAccessible(true);

            Object unmodifiableEnvironment = getenv.invoke(null);
            Class<?> map = Class.forName("java.util.Collections$UnmodifiableMap");
            Field m = map.getDeclaredField("m");
            m.setAccessible(true);

            return (Map<String, String>) m.get(unmodifiableEnvironment);
        }
    }

    @Nested
    final class Find {
        @Test
        void missing() {
            Binding[] bindings = new Binding[]{
                new MapBinding("test-name-1", Collections.emptyMap()),
            };

            assertThat(Bindings.find(bindings, "test-name-2")).isNull();
        }

        @Test
        @SuppressWarnings("ConstantConditions")
        void valid() {
            Binding[] bindings = new Binding[]{
                new MapBinding("test-name-1", Collections.emptyMap()),
                new MapBinding("test-name-2", Collections.emptyMap()),
            };

            assertThat(Bindings.find(bindings, "test-name-1").getName()).isEqualTo("test-name-1");
        }
    }

    @Nested
    final class Filter {
        @Test
        void none() {
            Binding[] b = new Binding[]{
                new MapBinding("test-name-1", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
                ),
                new MapBinding("test-name-2", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-3", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-4", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .asBytes()
                ),
            };

            assertThat(Bindings.filter(b, null, null)).hasSize(4);
        }

        @Test
        void type() {
            Binding[] b = new Binding[]{
                new MapBinding("test-name-1", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
                ),
                new MapBinding("test-name-2", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-3", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-4", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .asBytes()
                ),
            };

            assertThat(Bindings.filter(b, "test-type-1", null)).hasSize(2);
        }

        @Test
        void provider() {
            Binding[] b = new Binding[]{
                new MapBinding("test-name-1", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
                ),
                new MapBinding("test-name-2", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-3", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-4", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .asBytes()
                ),
            };

            assertThat(Bindings.filter(b, null, "test-provider-2")).hasSize(2);
        }

        @Test
        void typeAndProvider() {
            Binding[] b = new Binding[]{
                new MapBinding("test-name-1", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
                ),
                new MapBinding("test-name-2", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-3", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-4", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .asBytes()
                ),
            };

            assertThat(Bindings.filter(b, "test-type-1", "test-provider-1")).hasSize(1);
        }

        @Test
        void overload() {
            Binding[] b = new Binding[]{
                new MapBinding("test-name-1", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-1")
                    .asBytes()
                ),
                new MapBinding("test-name-2", new FluentMap()
                    .withEntry("type", "test-type-1")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                ),
                new MapBinding("test-name-3", new FluentMap()
                    .withEntry("type", "test-type-2")
                    .withEntry("provider", "test-provider-2")
                    .asBytes()
                )
            };

            assertThat(Bindings.filter(b, "test-type-1")).hasSize(2);
        }
    }
}
