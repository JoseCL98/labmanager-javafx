package com.myorg.labmanager.infrastructure.facade;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.domain.service.EquipoService;
import com.myorg.labmanager.domain.service.PrestamoService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteFacade {
    private final EquipoService equipoService;
    private final PrestamoService prestamoService;

    public ReporteFacade(EquipoService equipoService, PrestamoService prestamoService) {
        this.equipoService = equipoService;
        this.prestamoService = prestamoService;
    }

    /**
     * Genera un reporte completo del estado del inventario
     */
    public ReporteInventario generarReporteCompleto() {
        List<Equipo> equipos = equipoService.listAll();
        List<Prestamo> prestamos = prestamoService.listAll();
        
        int totalEquipos = equipos.size();
        int equiposDisponibles = (int) equipos.stream()
            .filter(Equipo::disponible)
            .count();
        
        int totalPrestamos = prestamos.size();
        long prestamosActivos = prestamos.stream()
            .filter(Prestamo::isPrestado)
            .count();
        
        List<Equipo> equiposBajoStock = equipos.stream()
            .filter(e -> e.getCantidadDisponible() <= 1 && e.getCantidadDisponible() > 0)
            .collect(Collectors.toList());
        
        List<Equipo> equiposAgotados = equipos.stream()
            .filter(e -> e.getCantidadDisponible() == 0)
            .collect(Collectors.toList());

        return new ReporteInventario(
            totalEquipos,
            equiposDisponibles,
            totalPrestamos,
            (int) prestamosActivos,
            equiposBajoStock,
            equiposAgotados,
            LocalDateTime.now()
        );
    }

    /**
     * Genera un reporte en formato texto legible
     */
    public String generarReporteTexto() {
        ReporteInventario reporte = generarReporteCompleto();
        StringBuilder sb = new StringBuilder();
        
        sb.append("-----------------------------------\n");
        sb.append("    REPORTE DE INVENTARIO - LAB MANAGER\n");
        sb.append("-----------------------------------\n");
        sb.append(String.format("Fecha: %s\n\n", reporte.getFechaGeneracion()));
        
        sb.append("ESTADÍSTICAS GENERALES:\n");
        sb.append(String.format("  • Total de equipos: %d\n", reporte.getTotalEquipos()));
        sb.append(String.format("  • Equipos con disponibilidad: %d\n", reporte.getEquiposDisponibles()));
        sb.append(String.format("  • Total de préstamos: %d\n", reporte.getTotalPrestamos()));
        sb.append(String.format("  • Préstamos activos: %d\n\n", reporte.getPrestamosActivos()));
        
        if (!reporte.getEquiposAgotados().isEmpty()) {
            sb.append("EQUIPOS AGOTADOS:\n");
            for (Equipo e : reporte.getEquiposAgotados()) {
                sb.append(String.format("  • %s - %s\n", e.getTipo(), e.getNombre()));
            }
            sb.append("\n");
        }
        
        if (!reporte.getEquiposBajoStock().isEmpty()) {
            sb.append("EQUIPOS CON BAJO STOCK:\n");
            for (Equipo e : reporte.getEquiposBajoStock()) {
                sb.append(String.format("  • %s - Solo %d disponible(s)\n", 
                    e.getNombre(), e.getCantidadDisponible()));
            }
            sb.append("\n");
        }
        
        sb.append("-----------------------------------\n");
        
        return sb.toString();
    }
    
    /**
     * Genera estadísticas por tipo de equipo
     */
    public String generarEstadisticasPorTipo() {
        List<Equipo> equipos = equipoService.listAll();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\nESTADÍSTICAS POR TIPO DE EQUIPO:\n");
        sb.append("─────────────────────────────────────\n");
        
        equipos.stream()
            .collect(Collectors.groupingBy(Equipo::getTipo))
            .forEach((tipo, lista) -> {
                int total = lista.stream().mapToInt(Equipo::getCantidadTotal).sum();
                int disponibles = lista.stream().mapToInt(Equipo::getCantidadDisponible).sum();
                int prestados = total - disponibles;
                
                sb.append(String.format("%s:\n", tipo));
                sb.append(String.format("  Total: %d | Disponibles: %d | Prestados: %d\n\n", 
                    total, disponibles, prestados));
            });
        
        return sb.toString();
    }
}
