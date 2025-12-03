
package com.myorg.labmanager.persistence.repository;

import com.myorg.labmanager.domain.model.Prestamo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryPrestamoRepository implements PrestamoRepository {
    private final Map<Integer, Prestamo> map = new ConcurrentHashMap<>();
    @Override public Prestamo save(Prestamo p) { map.put(p.getId(), p); return p; }
    @Override public Optional<Prestamo> findById(int id) { return Optional.ofNullable(map.get(id)); }
    @Override public List<Prestamo> findAll() { return new ArrayList<>(map.values()); }
    @Override public List<Prestamo> findByUsuarioId(int usuarioId) {
        return map.values().stream().filter(p->p.getUsuarioId()==usuarioId).collect(Collectors.toList());
    }
    @Override public void update(Prestamo p) { map.put(p.getId(), p); }

    @Override
    public List<Prestamo> findByEquipoId(int equipoId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Prestamo> findActivosByEquipoId(int equipoId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
