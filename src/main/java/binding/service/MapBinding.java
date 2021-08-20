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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * An implementation of {@link Binding} that returns values from a {@link Map}.
 */
public final class MapBinding implements Binding {

    private final String name;

    private final Map<String, byte[]> content;

    /**
     * Creates a new {@code MapBinding} instance.
     *
     * @param name    the name of the binding
     * @param content the content of the binding
     */
    public MapBinding(@NotNull String name, @NotNull Map<String, byte[]> content) {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(content, "content must not be null");

        this.name = name;
        this.content = content;
    }

    @Nullable
    @Override
    public byte[] getAsBytes(@NotNull String key) {
        Assert.notNull(key, "key must not be null");

        if (!Secret.isValidSecretKey(key)) {
            return null;
        }

        return content.get(key);
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapBinding that = (MapBinding) o;
        return name.equals(that.name);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Generated
    @NotNull
    @Override
    public String toString() {
        return "MapBinding{" +
            "name='" + name + '\'' +
            '}';
    }

}
