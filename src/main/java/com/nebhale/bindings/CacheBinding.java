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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An implementation of {@link Binding} that caches values once they've been retrieved.
 */
public final class CacheBinding implements Binding {

    private final Binding delegate;

    private final Map<String, byte[]> cache = new HashMap<>();

    /**
     * Creates a new {@code CacheBinding} instance.
     *
     * @param delegate the {@link Binding} used to retrieve original values
     */
    public CacheBinding(@NotNull Binding delegate) {
        this.delegate = delegate;
    }

    @Nullable
    @Override
    public byte[] getAsBytes(@NotNull String key) {
        return cache.computeIfAbsent(key, delegate::getAsBytes);
    }

    @NotNull
    @Override
    public String getName() {
        return delegate.getName();
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheBinding that = (CacheBinding) o;
        return delegate.equals(that.delegate);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    @Generated
    @NotNull
    @Override
    public String toString() {
        return "CacheBinding{" +
            "delegate=" + delegate +
            '}';
    }

}
