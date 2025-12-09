# Lab Manager – Gestor de Inventario de Laboratorio

> **Sistema de gestión de equipos, usuarios y préstamos en un laboratorio académico**, construido en **Java** con arquitectura limpia, persistencia relacional (H2 + JPA), y respeto riguroso a los principios **SOLID**, **GoF** y **GRASP**.

---

## Objetivos

Demostrar dominio avanzado de diseño orientado a objetos mediante un sistema realista que:

- Permite crear, leer, actualizar y eliminar **equipos**, **usuarios** y **préstamos**.
- Aplica reglas de negocio configurables.
- Notifica eventos (como stock bajo).
- Genera reportes completos.
- Soporta importación de datos desde CSV.

---

## Arquitectura y Tecnologías

| Capa | Tecnologías / Patrones |
|------|------------------------|
| **Frontend** | JavaFX (interfaz gráfica) |
| **Dominio** | Entidades JPA, servicios, estrategias, observadores |
| **Persistencia** | **H2 (modo archivo)** + **JPA (Hibernate)** |
| **Inyección de dependencias** | Contenedor manual (`Container`) |
| **Patrones aplicados** | Ver sección detallada abajo |
| **Base de datos** | Archivo local: `./data/labmanager.mv.db` |

---

## Principios y Patrones Aplicados

### - SOLID
- **SRP**: Cada clase tiene una única responsabilidad (ej. `PrestamoService` no maneja UI ni persistencia directa).
- **OCP**: Nuevas reglas de préstamo se añaden mediante `ReglaPrestamoStrategy`.
- **LSP**: Todas las implementaciones de repositorios cumplen su interfaz sin romper contratos.
- **ISP**: Interfaces pequeñas y específicas (`EquipoRepository`, `StockObserver`, `DataImporter`).
- **DIP**: Servicios dependen de abstracciones, no de implementaciones concretas.

### - Patrones GoF
| Categoría | Patrón | Ejemplo |
|----------|--------|--------|
| **Creacionales** | Factory Method | `EquipoFactory.create()` |
| | Builder | `PrestamoBuilder` |
| **Estructurales** | Adapter | `CsvToEquipoAdapter` |
| | Facade | `ReporteFacade` |
| **Comportamiento** | Strategy | `LimitePorUsuarioStrategy` |
| | Observer | `EventBus` + `LogNotifier` |

### - GRASP
- **Controller**: `MainController` y `RegistrarPrestamoController` coordinan la interacción usuario-sistema.
- **Information Expert**: `Equipo` sabe si está disponible; `Prestamo` sabe si puede devolverse.
- **Low Coupling / High Cohesion**: Clases bien encapsuladas y acopladas solo a interfaces.
- **Polymorphism**: Uso de estrategias y observadores.

---

## Funcionalidades

- ✅ **Gestión de equipos**: alta, edición, eliminación, importación CSV.
- ✅ **Gestión de usuarios**: estudiantes, profesores, etc.
- ✅ **Préstamos y devoluciones**: con validación de reglas y stock.
- ✅ **Notificaciones**: alertas por consola cuando el stock está bajo.
- ✅ **Reportes**: estado del inventario, equipos agotados, estadísticas por tipo.
- ✅ **Persistencia real**: usa **H2 en modo archivo** (los datos persisten entre ejecuciones).

---

## Estructura del Proyecto

```
src/
├── app/
│   └── controllers/         # Controladores de casos de uso
├── config/                  # Contenedor de dependencias
├── domain/
│   ├── model/               # Entidades (JPA)
│   ├── rule/                # Estrategias de negocio
│   └── service/             # Lógica de dominio
├── infra/
│   ├── notifier/            # Observer (EventBus, LogNotifier)
│   ├── adapter/             # CSV → Equipo (Adapter)
│   ├── facade/              # ReporteFacade
│   └── persistence/         # Repositorios JPA
├── persistence/             # Interfaces de repositorios
├── ui/                      # Controlador JavaFX
└── MainApp.java             # Punto de entrada
```

---

## Cómo Ejecutar

### Requisitos
- JDK 17+  
- Maven (para dependencias: Hibernate, H2, JavaFX)

### Pasos
1. Clonar el repositorio.
2. Ejecutar con tu IDE favorito (NetBeans 25 recomendado) o desde la terminal:
   ```bash
   mvn javafx:run
   ```
3. La base de datos se crea automáticamente en:
   ```
   ./data/labmanager.mv.db
   ```
4. La aplicación incluye datos de ejemplo (equipos y usuarios).

---

## Licencia

Este proyecto es de carácter académico. Libre para uso educativo.

---
