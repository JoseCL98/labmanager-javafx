package com.myorg.labmanager.infrastructure.observer;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.infra.notifier.StockObserver;

/**
 * Interface base para notificaciones
 */
public interface Notifier extends StockObserver {
    void notify(String message);
    
    @Override
    default void onStockLow(Equipo equipo) {
        notify("[ALERTA] Stock bajo: " + equipo.toString());
    }
}

/**
 * Implementación básica de consola
 */
class ConsoleNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[CONSOLA] " + message);
    }
}

/**
 * PATRÓN DECORATOR
 * Añade funcionalidad de notificación por email sin modificar el notificador base.
 * Permite combinar múltiples canales de notificación de forma flexible.
 */
class EmailNotifierDecorator implements Notifier {
    private final Notifier wrapped;

    public EmailNotifierDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void notify(String message) {
        // Primero ejecuta la notificación base
        wrapped.notify(message);
        
        // Luego añade la funcionalidad de email
        enviarEmail(message);
    }
    
    private void enviarEmail(String message) {
        // Simulación de envío de email
        System.out.println("[EMAIL] Enviando correo...");
        System.out.println("[EMAIL] Asunto: Alerta Lab Manager");
        System.out.println("[EMAIL] Mensaje: " + message);
        System.out.println("[EMAIL] ✓ Correo enviado exitosamente");
    }
}

/**
 * Otro ejemplo de Decorator para SMS
 */
class SmsNotifierDecorator implements Notifier {
    private final Notifier wrapped;

    public SmsNotifierDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void notify(String message) {
        wrapped.notify(message);
        enviarSms(message);
    }
    
    private void enviarSms(String message) {
        System.out.println("[SMS] Enviando mensaje de texto...");
        System.out.println("[SMS] " + message);
        System.out.println("[SMS] ✓ SMS enviado");
    }
}
