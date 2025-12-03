
package com.myorg.labmanager.domain.model;
import javax.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 30)
    private String tipo; // estudiante, profesor, etc.

    // Constructor vacío para JPA
    public Usuario() {}

    public Usuario(int id, String nombre, String tipo) { 
        this.id = id; 
        this.nombre = nombre; 
        this.tipo = tipo; 
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", nombre, tipo);
    }
}
