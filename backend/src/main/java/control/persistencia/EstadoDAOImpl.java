package control.persistencia;

import entity.Estado;
import javax.persistence.EntityManager;
import java.util.List;

public class EstadoDAOImpl implements EstadoDAO {

    @Override
    public List<Estado> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM Estado e";
            return em.createQuery(jpql, Estado.class).getResultList();
        } finally {
            em.close();
        }
    }
}
