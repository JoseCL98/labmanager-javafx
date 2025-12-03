package com.myorg.labmanager.domain.service;

import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.domain.model.PrestamoBuilder;
import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.rule.ReglaPrestamoStrategy;
import com.myorg.labmanager.infra.notifier.EventBus;
import com.myorg.labmanager.persistence.repository.EquipoRepository;
import com.myorg.labmanager.persistence.repository.PrestamoRepository;

import java.util.List;

public class PrestamoService {
    private final EquipoRepository equipoRepo;
    private final PrestamoRepository prestamoRepo;
    private final ReglaPrestamoStrategy regla;
    private final EventBus eventBus;

    public PrestamoService(EquipoRepository equipoRepo, 
                          PrestamoRepository prestamoRepo, 
                          ReglaPrestamoStrategy regla, 
                          EventBus eventBus) {
        this.equipoRepo = equipoRepo;
        this.prestamoRepo = prestamoRepo;
        this.regla = regla;
        this.eventBus = eventBus;
    }

    public boolean prestar(int equipoId, Usuario usuario, int cantidad) {
        Equipo equipo = equipoRepo.findById(equipoId).orElse(null);
        if (equipo == null) {
            return false;
        }
        
        if (!regla.puedePrestar(usuario, equipo, cantidad)) {
            return false;
        }
        
        // Decrementar disponibilidad
        equipo.decrementar(cantidad);
        equipoRepo.update(equipo);
        
        // Crear préstamo
        Prestamo prestamo = new PrestamoBuilder()
            .equipoId(equipoId)
            .usuarioId(usuario.getId())
            .cantidad(cantidad)
            .build();
        
        prestamoRepo.save(prestamo);
        
        // Notificar si stock bajo
        if (equipo.getCantidadDisponible() <= 1) {
            eventBus.notifyLowStock(equipo);
        }
        
        return true;
    }
    
    public boolean devolver(int prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId).orElse(null);
        if (prestamo == null || !prestamo.isPrestado()) {
            return false;
        }
        
        // Marcar como devuelto
        prestamo.devolver();
        prestamoRepo.update(prestamo);
        
        // Incrementar disponibilidad
        Equipo equipo = equipoRepo.findById(prestamo.getEquipoId()).orElse(null);
        if (equipo != null) {
            equipo.incrementar(prestamo.getCantidad());
            equipoRepo.update(equipo);
        }
        
        return true;
    }

    public List<Prestamo> listAll() {
        return prestamoRepo.findAll();
    }
    
    public List<Prestamo> listByUsuario(int usuarioId) {
        return prestamoRepo.findByUsuarioId(usuarioId);
    }
    
    public List<Prestamo> listActivosByEquipo(int equipoId) {
        return prestamoRepo.findActivosByEquipoId(equipoId);
    }
}
