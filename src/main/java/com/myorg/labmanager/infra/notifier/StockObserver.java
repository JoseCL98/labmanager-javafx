
package com.myorg.labmanager.infra.notifier;

import com.myorg.labmanager.domain.model.Equipo;

public interface StockObserver {
    void onStockLow(Equipo equipo);
}
