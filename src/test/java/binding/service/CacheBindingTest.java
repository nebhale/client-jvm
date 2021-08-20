package binding.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CacheBinding")
final class CacheBindingTest {

    private final StubBinding stub = new StubBinding();

    private final CacheBinding binding = new CacheBinding(stub);

    @Test
    @DisplayName("retrieves uncached value")
    void uncachedValue() {
        assertThat(binding.get("test-key")).isNotNull();
        assertThat(stub.getAsBytesCount).isEqualTo(1);
    }

    @Test
    @DisplayName("not cache unknown keys")
    void unknownKey() {
        assertThat(binding.get("test-unknown-key")).isNull();
        assertThat(binding.get("test-unknown-key")).isNull();
        assertThat(stub.getAsBytesCount).isEqualTo(2);
    }

    @Test
    @DisplayName("returns cached value")
    void cachedValue() {
        assertThat(binding.get("test-key")).isNotNull();
        assertThat(binding.get("test-key")).isNotNull();
        assertThat(stub.getAsBytesCount).isEqualTo(1);
    }

    @Test
    @DisplayName("always calls getName")
    void alwaysCallsGetName() {
        assertThat(binding.getName()).isNotNull();
        assertThat(binding.getName()).isNotNull();
        assertThat(stub.getNameCount).isEqualTo(2);
    }

    private static final class StubBinding implements Binding {

        private int getAsBytesCount = 0;

        private int getNameCount = 0;

        @Nullable
        @Override
        public byte[] getAsBytes(@NotNull String key) {
            getAsBytesCount++;

            if ("test-key".equals(key)) {
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
