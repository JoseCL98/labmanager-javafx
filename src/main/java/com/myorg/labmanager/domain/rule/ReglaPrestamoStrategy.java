
package com.myorg.labmanager.domain.rule;

import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.domain.model.Equipo;

public interface ReglaPrestamoStrategy {
    boolean puedePrestar(Usuario usuario, Equipo equipo, int cantidad);
}
