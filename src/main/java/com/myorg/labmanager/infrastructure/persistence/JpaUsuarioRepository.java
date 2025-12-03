package com.myorg.labmanager.infrastructure.persistence;

import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.persistence.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

public class JpaUsuarioRepository implements UsuarioRepository {
    private final EntityManager em;

    public JpaUsuarioRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Usuario save(Usuario u) {
        try {
            em.getTransaction().begin();
            em.merge(u);
            em.getTransaction().commit();
            return u;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar usuario", ex);
        }
    }

    @Override
    public Optional<Usuario> findById(int id) {
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
    }
    
    @Override
    public Optional<Usuario> findByNombre(String nombre) {
        try {
            Usuario usuario = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombre = :nombre", 
                    Usuario.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            return Optional.ofNullable(usuario);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Usuario u) {
        try {
            em.getTransaction().begin();
            em.merge(u);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar usuario", ex);
        }
    }

    @Override
    public void delete(int id) {
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar usuario", ex);
        }
    }
    
    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
    }
}
