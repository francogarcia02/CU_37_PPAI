package control.persistencia;
import entity.OrdenInspeccion;
import java.sql.Connection;

public class OrdenDAOImpl implements OrdenDAO {

    private Connection connection;

    public OrdenDAOImpl() {
        // Obtengo la conexion una sola vez
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public void actualizar(OrdenInspeccion ordenInspeccion) {
        //Implementar SQL ACA

        System.out.println("PERSISTENCIA: (TODO) Actualizando ORDEN " + ordenInspeccion.getNumeroOrden() + " en la BD.");
        // 1. (TODO) Escribir el UPDATE para T_ORDEN_INSPECCION
        // 2. (TODO) Escribir el UPDATE para T_SISMOGRAFO (o INSERT en T_CAMBIO_ESTADO)
        // 3. (TODO) Escribir el INSERT para T_MOTIVO_FS
    };

}
