package control.persistencia;

import entity.Estado;
import entity.MotivoFueraServicio;
import entity.OrdenInspeccion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OrdenDAOImpl implements OrdenDAO {

    @Override
    public boolean guardarCierre(OrdenInspeccion orden, Estado estadoSismografo, List<MotivoFueraServicio> motivos) {
        // Este método queda obsoleto, la lógica principal está en update.
        // Se podría eliminar en una futura refactorización.
        update(orden);
        return true;
    }

    @Override
    public List<OrdenInspeccion> getAllOrdenes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT o FROM OrdenInspeccion o";
            return em.createQuery(jpql, OrdenInspeccion.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(OrdenInspeccion orden) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Con las relaciones bidireccionales correctamente establecidas,
            // un simple merge es suficiente para que JPA sincronice todos los cambios.
            em.merge(orden);
            tx.commit();
            System.out.println("PERSISTENCIA: Orden " + orden.getNumeroOrden() + " actualizada con éxito.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("PERSISTENCIA: Error al actualizar la orden. Rollback ejecutado.");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
