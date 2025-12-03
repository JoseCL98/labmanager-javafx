
# Diseño y mapeo de patrones

- SRP: Cada servicio tiene responsabilidades únicas (EquipoService, PrestamoService).
- OCP: ReglaPrestamoStrategy permite extensiones sin modificar PrestamoService.
- LSP: Equipo se puede sustituir por subtipos (si se añaden).
- ISP: Repositorios con interfaces pequeñas.
- DIP: PrestamoService depende de abstracciones (interfaces repositorios).

Patrones GoF aplicados:
- Creacionales: Factory Method (EquipoFactory), Builder (PrestamoBuilder)
- Estructurales: Facade (Container/Sistema), Adapter (se puede añadir importer)
- Comportamiento: Strategy (ReglaPrestamoStrategy), Observer (EventBus/StockObserver)

GRASP:
- Controller: RegistrarPrestamoController
- Information Expert: Equipo (maneja disponibilidad)
- Creator: Repositorios guardan entidades
