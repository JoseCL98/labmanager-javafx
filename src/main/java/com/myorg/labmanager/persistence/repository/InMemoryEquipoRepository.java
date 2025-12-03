
package com.myorg.labmanager.persistence.repository;

import com.myorg.labmanager.domain.model.Equipo;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEquipoRepository implements EquipoRepository {
    private final Map<Integer, Equipo> map = new ConcurrentHashMap<>();

    @Override public Equipo save(Equipo e) { map.put(e.getId(), e); return e; }
    @Override public Optional<Equipo> findById(int id) { return Optional.ofNullable(map.get(id)); }
    @Override public List<Equipo> findAll() { return new ArrayList<>(map.values()); }
    @Override public void update(Equipo e) { map.put(e.getId(), e); }
    @Override public void delete(int id) { map.remove(id); }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
