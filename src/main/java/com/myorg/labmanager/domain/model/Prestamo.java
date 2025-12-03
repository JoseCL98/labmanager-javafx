
package com.myorg.labmanager.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prestamos")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private int equipoId;
    
    @Column(nullable = false)
    private int usuarioId;
    
    @Column(nullable = false)
    private LocalDateTime fechaPrestamo;
    
    @Column
    private LocalDateTime fechaDevolucion;
    
    @Column(nullable = false, length = 20)
    private String estado;
    
    @Column(nullable = false)
    private int cantidad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Constructor vacío para JPA
    public Prestamo() {}

    public Prestamo(int id, int equipoId, int usuarioId, LocalDateTime fechaPrestamo, int cantidad) {
        this.id = id;
        this.equipoId = equipoId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.estado = "PRESTADO";
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getEquipoId() { return equipoId; }
    public void setEquipoId(int equipoId) { this.equipoId = equipoId; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public LocalDateTime getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    
    public LocalDateTime getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    // Métodos de negocio
    public void devolver() { 
        this.fechaDevolucion = LocalDateTime.now(); 
        this.estado = "DEVUELTO"; 
    }
    
    public boolean isPrestado() {
        return "PRESTADO".equals(estado);
    }
}
