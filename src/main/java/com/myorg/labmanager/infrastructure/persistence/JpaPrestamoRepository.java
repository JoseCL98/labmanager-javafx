package com.myorg.labmanager.infrastructure.persistence;

import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.persistence.repository.PrestamoRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

public class JpaPrestamoRepository implements PrestamoRepository {
    private final EntityManager em;

    public JpaPrestamoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Prestamo save(Prestamo p) {
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
            return p;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar préstamo", ex);
        }
    }

    @Override
    public Optional<Prestamo> findById(int id) {
        try {
            Prestamo prestamo = em.find(Prestamo.class, id);
            return Optional.ofNullable(prestamo);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Prestamo> findAll() {
        return em.createQuery("SELECT p FROM Prestamo p", Prestamo.class)
                .getResultList();
    }

    @Override
    public List<Prestamo> findByUsuarioId(int usuarioId) {
        return em.createQuery(
                "SELECT p FROM Prestamo p WHERE p.usuarioId = :uid", 
                Prestamo.class)
                .setParameter("uid", usuarioId)
                .getResultList();
    }
    
    @Override
    public List<Prestamo> findByEquipoId(int equipoId) {
        return em.createQuery(
                "SELECT p FROM Prestamo p WHERE p.equipoId = :eid", 
                Prestamo.class)
                .setParameter("eid", equipoId)
                .getResultList();
    }
    
    @Override
    public List<Prestamo> findActivosByEquipoId(int equipoId) {
        return em.createQuery(
                "SELECT p FROM Prestamo p WHERE p.equipoId = :eid AND p.estado = 'PRESTADO'", 
                Prestamo.class)
                .setParameter("eid", equipoId)
                .getResultList();
    }

    @Override
    public void update(Prestamo p) {
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar préstamo", ex);
        }
    }
}
