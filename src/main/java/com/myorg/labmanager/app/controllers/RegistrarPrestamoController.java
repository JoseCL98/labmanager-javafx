
package com.myorg.labmanager.app.controllers;

import com.myorg.labmanager.domain.service.PrestamoService;
import com.myorg.labmanager.domain.model.Usuario;

public class RegistrarPrestamoController {
    private final PrestamoService prestamoService;

    public RegistrarPrestamoController(PrestamoService prestamoService) { this.prestamoService = prestamoService; }

    public boolean ejecutar(int equipoId, Usuario usuario, int cantidad) {
        return prestamoService.prestar(equipoId, usuario, cantidad);
    }
}
