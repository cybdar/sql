package netology.mode;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbInteraction {

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app",
                "app",
                "pass"
        );
    }
}
