
package com.myorg.labmanager.domain.rule;

import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.domain.model.Equipo;

public class LimitePorUsuarioStrategy implements ReglaPrestamoStrategy {
    private final int limite;

    public LimitePorUsuarioStrategy(int limite) { this.limite = limite; }

    @Override
    public boolean puedePrestar(Usuario usuario, Equipo equipo, int cantidad) {
        // regla simple: no prestar más que el límite
        return cantidad <= limite && equipo.disponible() && equipo.getCantidadDisponible() >= cantidad;
    }
}
