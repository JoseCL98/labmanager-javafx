
package com.myorg.labmanager.domain.model;

import java.time.LocalDateTime;

public class PrestamoBuilder {
    private int equipoId;
    private int usuarioId;
    private int cantidad = 1;
    private LocalDateTime fechaPrestamo = LocalDateTime.now();

    public PrestamoBuilder equipoId(int id) { 
        this.equipoId = id; 
        return this; 
    }
    
    public PrestamoBuilder usuarioId(int id) { 
        this.usuarioId = id; 
        return this; 
    }
    
    public PrestamoBuilder cantidad(int cantidad) {
        this.cantidad = cantidad;
        return this;
    }
    
    public PrestamoBuilder fechaPrestamo(LocalDateTime fecha) {
        this.fechaPrestamo = fecha;
        return this;
    }
    
    public Prestamo build() { 
        return new Prestamo(0, equipoId, usuarioId, fechaPrestamo, cantidad); 
    }
}
