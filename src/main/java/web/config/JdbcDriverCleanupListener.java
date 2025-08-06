package web.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@WebListener
public class JdbcDriverCleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Инициализация (не требуется)
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Очистка JDBC драйверов
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            if (driver instanceof org.postgresql.Driver) {
                try {
                    DriverManager.deregisterDriver(driver);
                    System.out.println("PostgreSQL JDBC Driver deregistered successfully");
                } catch (SQLException e) {
                    System.err.println("Error deregistering PostgreSQL JDBC Driver: " + e.getMessage());
                }
            }
        }
    }
}