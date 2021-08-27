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

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

final class ConfigTreeBindingTest {
    @Test
    void missing() {
        ConfigTreeBinding b = new ConfigTreeBinding(Paths.get("src/test/resources/test-k8s"));
        assertThat(b.getAsBytes("test-missing-key")).isNull();
    }

    @Test
    void directory() {
        ConfigTreeBinding b = new ConfigTreeBinding(Paths.get("src/test/resources/test-k8s"));
        assertThat(b.getAsBytes(".hidden-data")).isNull();
    }

    @Test
    void invalid() {
        ConfigTreeBinding b = new ConfigTreeBinding(Paths.get("src/test/resources/test-k8s"));
        assertThat(b.getAsBytes("test^invalid^key")).isNull();
    }

    @Test
    void valid() {
        ConfigTreeBinding b = new ConfigTreeBinding(Paths.get("src/test/resources/test-k8s"));
        assertThat(b.getAsBytes("test-secret-key")).isEqualTo("test-secret-value\n".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void getName() {
        ConfigTreeBinding b = new ConfigTreeBinding(Paths.get("src/test/resources/test-k8s"));
        assertThat(b.getName()).isEqualTo("test-k8s");
    }
}
