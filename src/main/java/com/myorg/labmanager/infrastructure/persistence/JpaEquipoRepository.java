package com.myorg.labmanager.infrastructure.persistence;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.persistence.repository.EquipoRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

public class JpaEquipoRepository implements EquipoRepository {
    private final EntityManager em;

    public JpaEquipoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Equipo save(Equipo e) {
        try {
            em.getTransaction().begin();
            em.merge(e);
            em.getTransaction().commit();
            return e;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar equipo", ex);
        }
    }

    @Override
    public Optional<Equipo> findById(int id) {
        try {
            Equipo equipo = em.find(Equipo.class, id);
            return Optional.ofNullable(equipo);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Equipo> findAll() {
        return em.createQuery("SELECT e FROM Equipo e", Equipo.class)
                .getResultList();
    }

    @Override
    public void update(Equipo e) {
        try {
            em.getTransaction().begin();
            em.merge(e);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar equipo", ex);
        }
    }

    @Override
    public void delete(int id) {
        try {
            em.getTransaction().begin();
            Equipo equipo = em.find(Equipo.class, id);
            if (equipo != null) {
                em.remove(equipo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar equipo", ex);
        }
    }
    
    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM Equipo e", Long.class).getSingleResult();
    }
}
