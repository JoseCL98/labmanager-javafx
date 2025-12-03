package com.myorg.labmanager.infrastructure.facade;

import com.myorg.labmanager.domain.model.Equipo;
import java.time.LocalDateTime;
import java.util.List;

public class ReporteInventario {
    private final int totalEquipos;
    private final int equiposDisponibles;
    private final int totalPrestamos;
    private final int prestamosActivos;
    private final List<Equipo> equiposBajoStock;
    private final List<Equipo> equiposAgotados;
    private final LocalDateTime fechaGeneracion;

    public ReporteInventario(int totalEquipos, int equiposDisponibles, 
                            int totalPrestamos, int prestamosActivos,
                            List<Equipo> equiposBajoStock, List<Equipo> equiposAgotados,
                            LocalDateTime fechaGeneracion) {
        this.totalEquipos = totalEquipos;
        this.equiposDisponibles = equiposDisponibles;
        this.totalPrestamos = totalPrestamos;
        this.prestamosActivos = prestamosActivos;
        this.equiposBajoStock = equiposBajoStock;
        this.equiposAgotados = equiposAgotados;
        this.fechaGeneracion = fechaGeneracion;
    }

    // Getters
    public int getTotalEquipos() { return totalEquipos; }
    public int getEquiposDisponibles() { return equiposDisponibles; }
    public int getTotalPrestamos() { return totalPrestamos; }
    public int getPrestamosActivos() { return prestamosActivos; }
    public List<Equipo> getEquiposBajoStock() { return equiposBajoStock; }
    public List<Equipo> getEquiposAgotados() { return equiposAgotados; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
}
