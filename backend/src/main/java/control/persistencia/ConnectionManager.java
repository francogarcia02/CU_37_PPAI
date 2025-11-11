package control.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    // AJUSTA ESTOS 3 VALORES
    private static final String SERVER_NAME = "LAPTOP-FITVVSD"; // <-- CAMBIÁ ESTO (Tu server name de SSMS)
    private static final String DATABASE_NAME = "PPAI_SISMOS"; // <-- CAMBIÁ ESTO (La BD que creaste)
    private static final String PASS = "TuContraseñaSQL"; // <-- CAMBIÁ ESTO (La clave que le pusiste a 'sa')

    // Formato estandar para conectar a sql server.
    private static final String URL = String.format("jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=true;", SERVER_NAME, DATABASE_NAME);
    // LOGIN con SQL SERVER AUTHENTICATION y un usuario administrador.
    private static final String USER = "sa";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("¡Conexión a SQL Server (" + SERVER_NAME + ") exitosa!");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al conectar a SQL Server. Revisa tu URL, User, Pass y que el servicio esté corriendo.");
            }
        }
        return connection;
    }

}
