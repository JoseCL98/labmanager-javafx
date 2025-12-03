
package com.myorg.labmanager.persistence.repository;

import com.myorg.labmanager.domain.model.Prestamo;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository {
    Prestamo save(Prestamo p);
    Optional<Prestamo> findById(int id);
    List<Prestamo> findAll();
    List<Prestamo> findByUsuarioId(int usuarioId);
    List<Prestamo> findByEquipoId(int equipoId);
    List<Prestamo> findActivosByEquipoId(int equipoId);
    void update(Prestamo p);
}
