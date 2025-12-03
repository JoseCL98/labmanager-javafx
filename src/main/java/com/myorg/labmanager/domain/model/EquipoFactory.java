
package com.myorg.labmanager.domain.model;

public class EquipoFactory {
    public static Equipo create(String tipo, String nombre, int cantidadTotal) {
        return new Equipo(0, tipo, nombre, cantidadTotal);
    }
}
