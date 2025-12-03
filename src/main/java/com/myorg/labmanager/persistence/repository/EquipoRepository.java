
package com.myorg.labmanager.persistence.repository;

import com.myorg.labmanager.domain.model.Equipo;
import java.util.List;
import java.util.Optional;

public interface EquipoRepository {
    Equipo save(Equipo e);
    Optional<Equipo> findById(int id);
    List<Equipo> findAll();
    void update(Equipo e);
    void delete(int id);
    long count();
}
