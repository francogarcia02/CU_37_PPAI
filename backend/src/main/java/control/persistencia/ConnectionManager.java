package control.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "PPAI_SISMOS";
    // LOGIN con SQL SERVER AUTHENTICATION y un usuario administrador.
    private static final String PASS = "ppai2025";
    private static final String USER = "sa";

    // Formato estandar para conectar a sql server.
    private static final String URL = String.format(
            "jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=true;",
            SERVER_NAME,
            DATABASE_NAME
    );

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("¡Conexion a SQL Server (" + SERVER_NAME + ") exitosa!");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al conectar a SQL Server. Revisa tu URL, User, Pass y que el servicio este corriendo.");
            }
        }
        return connection;
    }

}
