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

/**
 * A representation of a binding as defined by the
 * <a href="https://github.com/k8s-service-bindings/spec#workload-projection">Kubernetes Service Binding Specification</a>.
 */
public interface Binding {

    /**
     * The key for the provider of a binding.
     */
    String PROVIDER = "provider";

    /**
     * The key for the type of a binding.
     */
    String TYPE = "type";

    /**
     * Returns the contents of a binding entry in its raw {@code byte[]} form.
     *
     * @param key the key of the entry to retrieve
     * @return the contents of a binding entry if it exists, otherwise {@code null}
     */
    @Nullable
    byte[] getAsBytes(@NotNull String key);

    /**
     * Returns the name of the binding.
     *
     * @return the name of the binding
     */
    @NotNull
    String getName();

    /**
     * Returns the contents of a binding entry as a UTF-8 decoded {@code String}.  Any whitespace is trimmed.
     *
     * @param key the key of the entry to retrieve
     * @return the contents of a binding entry as a UTF-8 decoded {@code String} if it exists, otherwise {@code null}
     */
    @Nullable
    default String get(@NotNull String key) {
        Assert.notNull(key, "key must not be null");

        byte[] value = getAsBytes(key);

        if (value == null) {
            return null;
        }

        return new String(value, StandardCharsets.UTF_8).trim();
    }

    /**
     * Returns the value of the {@link #PROVIDER} key.
     *
     * @return the value of the {@link #PROVIDER} key if it exists, otherwise {@code null}
     */
    @Nullable
    default String getProvider() {
        return get(PROVIDER);
    }

    /**
     * Returns the value of the {@link #TYPE} key.
     *
     * @return the value of the {@link #TYPE} key
     * @throws IllegalStateException if the {@link #TYPE} key does not exist.
     */
    @NotNull
    default String getType() {
        String value = get(TYPE);

        if (value == null) {
            throw new IllegalStateException("binding does not contain a type");
        }

        return value;
    }

}
