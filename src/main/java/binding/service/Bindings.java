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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * A representation of a collection of bindings as defined by the
 * <a href="https://github.com/k8s-service-bindings/spec#workload-projection">Kubernetes Service Binding Specification</a>.
 */
public final class Bindings {

    /**
     * The name of the environment variable to read to determine the bindings file system root.  Specified by the
     * Kubernetes Service Binding Specification.
     */
    public static final String SERVICE_BINDING_ROOT = "SERVICE_BINDING_ROOT";

    private Bindings() {
    }

    /**
     * Wraps each {@link Binding} in a {@link CacheBinding}.
     *
     * @param bindings the {@link Binding}s to wrap
     * @return the wrapped {@link Binding}s
     */
    @NotNull
    public static Binding[] cached(@NotNull Binding[] bindings) {
        Assert.notNull(bindings, "bindings must not be null");

        return Stream.of(bindings)
            .map(CacheBinding::new)
            .toArray(Binding[]::new);
    }

    /**
     * Creates a collection of {@link Binding}s, from the specified path.  If the directory does not exist, an empty
     * collection is returned.
     *
     * @param root the root to populate the {@link Binding}s from
     */
    @NotNull
    public static Binding[] from(@NotNull Path root) {
        Assert.notNull(root, "root must not be null");

        if (!Files.exists(root) || !Files.isDirectory(root)) {
            return new Binding[0];
        }

        try {
            return Files.list(root)
                .filter(Files::isDirectory)
                .map(ConfigTreeBinding::new)
                .toArray(Binding[]::new);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("unable to list children of '%s'", root), e);
        }
    }


    /**
     * Creates a new collection of {@link Binding}s using the {@code $SERVICE_BINDING_ROOT} environment variable to
     * determine the file system root.  If the {@code $SERVICE_BINDING_ROOT} environment variables is not set, an empty
     * collection is returned. If the directory does not exist, an empty collection is returned.
     */
    public static Binding[] fromServiceBindingRoot() {
        String root = System.getenv(SERVICE_BINDING_ROOT);
        if (root == null) {
            return new Binding[0];
        }

        return from(Paths.get(root));
    }

    /**
     * Returns a {@link Binding} with a given name. Comparison is case-insensitive.
     *
     * @param bindings the {@link Binding}s to find in
     * @param name     the name of the {@code Binding} to find
     * @return the {@code Binding} with a given name if it exists, {@code null} otherwise
     */
    @Nullable
    public static Binding find(@NotNull Binding[] bindings, @NotNull String name) {
        Assert.notNull(bindings, "bindings must not be null");
        Assert.notNull(name, "name must not be null");

        return Stream.of(bindings)
            .filter(binding -> binding.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns zero or more {@link Binding}s with a given type.  Equivalent to
     * {@link #filter(Binding[], String, String)} with a {@code null} {@code provider}.
     *
     * @param bindings the {@link Binding}s to filter
     * @param type     the type of the {@code Binding} to find
     * @return the collection of {@code Binding}s with a given type
     */
    @NotNull
    public static Binding[] filter(@NotNull Binding[] bindings, @Nullable String type) {
        return filter(bindings, type, null);
    }

    /**
     * Return zero or more {@link Binding}s with a given type and provider.  If {@code type} or {@code provider} are
     * {@code null}, the result is not filtered on that argument.  Comparisons are case-insensitive.
     *
     * @param bindings the {@link Binding}s to filter
     * @param type     the type of {@code Binding} to find
     * @param provider the provider of {@code Binding} to find
     * @return the collection of {@code Binding}s with a given type and provider
     */
    @NotNull
    public static Binding[] filter(@NotNull Binding[] bindings, @Nullable String type, @Nullable String provider) {
        Assert.notNull(bindings, "bindings must not be null");

        return Stream.of(bindings)
            .filter(b -> type == null || b.getType().equalsIgnoreCase(type))
            .filter(b -> provider == null || b.getProvider() != null && b.getProvider().equalsIgnoreCase(provider))
            .toArray(Binding[]::new);
    }

}
