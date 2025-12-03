
package com.myorg.labmanager.domain.service;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.infra.notifier.EventBus;
import com.myorg.labmanager.persistence.repository.EquipoRepository;
import com.myorg.labmanager.persistence.repository.PrestamoRepository;

import java.util.List;

public class EquipoService {
    private final EquipoRepository repo;
    private final PrestamoRepository prestamoRepo;
    private final EventBus eventBus;

    public EquipoService(EquipoRepository repo, EventBus eventBus, PrestamoRepository prestamoRepo) { this.repo = repo; this.eventBus = eventBus; this.prestamoRepo = prestamoRepo; }

    public Equipo create(String tipo, String nombre, int cantidad) {
        Equipo e = new Equipo(0, tipo, nombre,cantidad);
        return repo.save(e);
    }
    
    public void update(int id, String nombre, String tipo, int cantidadTotal) {
        repo.findById(id).ifPresent(e -> {
        e.setNombre(nombre);
        e.setTipo(tipo);
        int diff = cantidadTotal - e.getCantidadTotal();
        e.setCantidadTotal(cantidadTotal);
        if (diff > 0) {
            e.incrementar(diff);
        } else if (diff < 0) {
            int reduccion = Math.min(-diff, e.getCantidadDisponible());
            e.decrementar(reduccion);
        }
        repo.update(e);
        });
    }
    
    public boolean delete(int id) {
        boolean tieneActivos = prestamoRepo.findAll().stream()
        .anyMatch(p -> p.getEquipoId() == id && "PRESTADO".equals(p.getEstado()));
        if (tieneActivos) return false;
        repo.delete(id);
        return true;
    }

    public List<Equipo> listAll() { return repo.findAll(); }

    public void decrement(int equipoId, int n) {
        repo.findById(equipoId).ifPresent(e -> { e.decrementar(n); repo.update(e); if (e.getCantidadDisponible() <= 1) eventBus.notifyLowStock(e); });
    }

    public void increment(int equipoId, int n) {
        repo.findById(equipoId).ifPresent(e -> { e.incrementar(n); repo.update(e); });
    }
}
