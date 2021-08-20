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

/**
 * An implementation of {@link Binding} that reads files from a
 * <a href="https://kubernetes.io/docs/concepts/configuration/secret/#using-secrets">volume mounted</a> Kubernetes
 * Secret.
 */
public final class ConfigTreeBinding implements Binding {

    private final Path root;

    /**
     * Creates a new {@code ConfigTreeBinding} instance.
     *
     * @param root the root of the volume mounted Kubernetes Secret
     */
    public ConfigTreeBinding(@NotNull Path root) {
        Assert.notNull(root, "root must not be null");
        this.root = root;
    }

    @Nullable
    @Override
    public byte[] getAsBytes(@NotNull String key) {
        Assert.notNull(key, "key must not be null");

        if (!Secret.isValidSecretKey(key)) {
            return null;
        }

        Path path = root.resolve(key);

        if (!Files.exists(path)) {
            return null;
        }

        if (!Files.isRegularFile(path)) {
            return null;
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("unable to read file '%s'", path), e);
        }
    }

    @NotNull
    @Override
    public String getName() {
        return root.getFileName().toString();
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigTreeBinding binding = (ConfigTreeBinding) o;
        return root.equals(binding.root);
    }

    @Generated
    @Override
    public int hashCode() {
        return java.util.Objects.hash(root);
    }

    @Generated
    @NotNull
    @Override
    public String toString() {
        return "Binding{" +
            "root=" + root +
            '}';
    }

}
