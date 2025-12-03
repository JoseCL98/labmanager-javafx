package com.myorg.labmanager.persistence.repository;

import com.myorg.labmanager.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario u);
    Optional<Usuario> findById(int id);
    Optional<Usuario> findByNombre(String nombre);
    List<Usuario> findAll();
    void update(Usuario u);
    void delete(int id);
    long count();
}
