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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

final class FluentMap extends HashMap<String, String> {

    @NotNull
    public FluentMap withEntry(@NotNull String key, @Nullable String value) {
        Assert.notNull(key, "key must not be null");

        put(key, value);
        return this;
    }

    public Map<String, byte[]> asBytes() {
        return this.entrySet().stream()
            .collect(Collectors.toMap(
                Entry::getKey,
                e -> e.getValue().getBytes(StandardCharsets.UTF_8)
            ));
    }

}
