package control.persistencia;

import entity.Estado;
import entity.MotivoFueraServicio;
import entity.OrdenInspeccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrdenDAOImpl implements OrdenDAO {

    private Connection connection;

    public OrdenDAOImpl() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public boolean guardarCierre(OrdenInspeccion orden, Estado estadoSismografo, List<MotivoFueraServicio> motivos) {

        try {
            connection.setAutoCommit(false); // Iniciar Transacción

            // --- TAREA 1: Actualizar la T_ORDEN_INSPECCION ---
            String sqlUpdateOrden = "UPDATE T_ORDEN_INSPECCION SET observaciones_cierre = ?, id_estado = ? WHERE id_orden = ?";
            try (PreparedStatement pstOrden = connection.prepareStatement(sqlUpdateOrden)) {

                // CORREGIDO: Usa los métodos de tu clase OrdenInspeccion
                pstOrden.setString(1, orden.getObservaciones());
                // Asume que el estado de la orden ya se actualizó en el objeto 'orden'
                pstOrden.setInt(2, orden.obtenerCambioEstadoActual().getEstadoNuevo().getIdEstado()); // <-- ARREGLADO
                pstOrden.setLong(3, orden.getNumeroOrden());

                pstOrden.executeUpdate();
            }

            // --- TAREA 2: Actualizar el Sismógrafo y guardar Motivos (SI HAY) ---
            if (motivos != null && !motivos.isEmpty()) {

                // --- TAREA 2a: Actualizar T_SISMOGRAFO ---
                String sqlUpdateSismo = "UPDATE T_SISMOGRAFO SET id_estado = ? WHERE id_sismografo = ?";
                try (PreparedStatement pstSismo = connection.prepareStatement(sqlUpdateSismo)) {

                    // CORREGIDO: Ahora 'estadoSismografo' tiene el getter
                    pstSismo.setInt(1, estadoSismografo.getIdEstado()); // <-- ARREGLADO
                    pstSismo.setLong(2, orden.getEstacionSismologica().getSismografo().getIdSismografo());

                    pstSismo.executeUpdate();
                }

                // --- TAREA 2b: Insertar en T_MOTIVO_FS ---
                String sqlInsertMotivo = "INSERT INTO T_MOTIVO_FS (comentario, id_orden_inspeccion, id_tipo_motivo) VALUES (?, ?, ?)";
                try (PreparedStatement pstMotivo = connection.prepareStatement(sqlInsertMotivo)) {

                    for (MotivoFueraServicio motivo : motivos) {
                        pstMotivo.setString(1, motivo.getComentario());
                        pstMotivo.setLong(2, orden.getNumeroOrden());

                        // CORREGIDO: Ahora 'motivo.getTipoMotivo()' tiene el getter
                        pstMotivo.setInt(3, motivo.getTipoMotivo().getIdTipoMotivo()); // <-- ARREGLADO

                        pstMotivo.addBatch();
                    }
                    pstMotivo.executeBatch();
                }
            }

            // Si todo salió bien, confirmamos los cambios en la BD
            connection.commit();
            System.out.println("PERSISTENCIA: Cierre de Orden " + orden.getNumeroOrden() + " guardado con ÉXITO.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                System.err.println("PERSISTENCIA: Rollback ejecutado. Error al guardar.");
            } catch (SQLException eRollback) {
                eRollback.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Volvemos al modo normal
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}