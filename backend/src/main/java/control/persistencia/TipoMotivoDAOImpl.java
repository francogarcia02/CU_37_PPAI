package control.persistencia;

import entity.TipoMotivo;
import javax.persistence.EntityManager;
import java.util.List;

public class TipoMotivoDAOImpl implements TipoMotivoDAO {

    @Override
    public List<TipoMotivo> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT t FROM TipoMotivo t";
            return em.createQuery(jpql, TipoMotivo.class).getResultList();
        } finally {
            em.close();
        }
    }
}
