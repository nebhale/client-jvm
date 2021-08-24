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

package example;

import com.nebhale.bindings.Binding;
import com.nebhale.bindings.Bindings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Example {

    public static void main(String[] args) {
        Binding[] bindings = Bindings.fromServiceBindingRoot();
        bindings = Bindings.filter(bindings, "postgresql");
        if (bindings.length != 1) {
            System.err.printf("Incorrect number of PostgreSQL bindings: %d\n", bindings.length);
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
