
package com.myorg.labmanager.infra.notifier;

import com.myorg.labmanager.domain.model.Equipo;
import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private final List<StockObserver> observers = new ArrayList<>();
    public void register(StockObserver o) { observers.add(o); }
    public void notifyLowStock(Equipo e) { for (StockObserver o : observers) o.onStockLow(e); }
}
