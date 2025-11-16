package control.persistencia;

import entity.Empleado;
import javax.persistence.EntityManager;
import java.util.List;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public Empleado getById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Empleado> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM Empleado e";
            return em.createQuery(jpql, Empleado.class).getResultList();
        } finally {
            em.close();
        }
    }
}
