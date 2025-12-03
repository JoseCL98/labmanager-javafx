
package com.myorg.labmanager.domain.model;
import javax.persistence.*;

@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false)
    private int cantidadTotal;
    
    @Column(nullable = false)
    private int cantidadDisponible;

    // Constructor vacío para JPA
    public Equipo() {}

    public Equipo(int id, String tipo, String nombre, int cantidadTotal) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadTotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getCantidadTotal() { return cantidadTotal; }
    public void setCantidadTotal(int cantidadTotal) { this.cantidadTotal = cantidadTotal; }
    
    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    // Métodos de negocio
    public boolean disponible() { 
        return cantidadDisponible > 0; 
    }

    public void decrementar(int n) { 
        if (n <= cantidadDisponible) {
            cantidadDisponible -= n; 
        }
    }
    
    public void incrementar(int n) { 
        cantidadDisponible += n; 
        if (cantidadDisponible > cantidadTotal) {
            cantidadDisponible = cantidadTotal; 
        }
    }

    @Override
    public String toString() { 
        return String.format("%s (%s) - %d/%d disponibles", 
            nombre, tipo, cantidadDisponible, cantidadTotal); 
    }
}
