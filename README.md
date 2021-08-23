# client-jvm

[![Tests](https://github.com/nebhale/client-jvm/workflows/Tests/badge.svg?branch=main)](https://github.com/nebhale/client-jvm/actions/workflows/tests.yaml)
[![codecov](https://codecov.io/gh/nebhale/client-jvm/branch/main/graph/badge.svg)](https://codecov.io/gh/nebhale/client-jvm)

`client-jvm` is a library to access [Service Binding Specification for Kubernetes](https://k8s-service-bindings.github.io/spec/) conformant Service Binding [Workload Projections](https://k8s-service-bindings.github.io/spec/#workload-projection).

## Example

```java
import com.nebhale.bindings.Binding;
import com.nebhale.bindings.Bindings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) {
        Binding[] bindings = Bindings.fromServiceBindingRoot();
        bindings = Bindings.filter(bindings, "postgresql");
        if (bindings.length != 1) {
            System.err.printf("Incorrect number of PostgreSQL drivers: %d\n", bindings.length);
            System.exit(1);
        }

        String url = bindings[0].get("url");
        if (url == null) {
            System.err.println("No URL in binding");
            System.exit(1);
        }

        Connection conn;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.printf("Unable to connect to database: %s", e);
            System.exit(1);
        }

        // ...
    }

}
```

## License

Apache License v2.0: see [LICENSE](./LICENSE) for details.
