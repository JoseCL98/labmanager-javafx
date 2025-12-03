
package com.myorg.labmanager.infra.notifier;

import com.myorg.labmanager.domain.model.Equipo;

public class LogNotifier implements StockObserver {
    @Override
    public void onStockLow(Equipo equipo) {
        System.out.println("[ALERTA] Stock bajo: " + equipo);
    }
}
