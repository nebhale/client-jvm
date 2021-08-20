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
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Binding")
final class BindingTest {

    @Test
    @DisplayName("missing key")
    void missingKey() {
        Binding b = new MapBinding("test-name", Collections.emptyMap());

        assertThat(b.get("test-key")).isNull();
    }

    @Test
    @DisplayName("valid key")
    void valid() {
        Binding b = new MapBinding("test-name",
            new FluentMap()
                .withEntry("test-key", "test-value")
                .asBytes());

        assertThat(b.get("test-key")).isEqualTo("test-value");
    }

    @Test
    @DisplayName("get provider doesn't exist")
    void getProviderNonExist() {
        Binding b = new MapBinding("test-name", Collections.emptyMap());

        assertThat(b.getProvider()).isNull();
    }

    @Test
    @DisplayName("get provider exists")
    void getProviderExists() {
        Binding b = new MapBinding("test-name",
            new FluentMap()
                .withEntry("provider", "test-provider")
                .asBytes());

        assertThat(b.getProvider()).isEqualTo("test-provider");
    }

    @Test
    @DisplayName("get type invalid binding")
    void getTypeInvalidBinding() {
        Binding b = new MapBinding("test-name", Collections.emptyMap());

        assertThatIllegalStateException().isThrownBy(b::getType);
    }

    @Test
    @DisplayName("get type valid binding")
    void getTypeValidBinding() {
        Binding b = new MapBinding("test-name",
            new FluentMap()
                .withEntry("type", "test-type")
                .asBytes());

        assertThat(b.getType()).isEqualTo("test-type");
    }

}
