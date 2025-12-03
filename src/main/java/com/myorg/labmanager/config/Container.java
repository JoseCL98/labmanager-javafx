
package com.myorg.labmanager.config;

import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.model.EquipoFactory;
import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.domain.service.EquipoService;
import com.myorg.labmanager.domain.service.PrestamoService;
import com.myorg.labmanager.domain.rule.LimitePorUsuarioStrategy;
import com.myorg.labmanager.domain.rule.ReglaPrestamoStrategy;
import com.myorg.labmanager.domain.service.UsuarioService;
import com.myorg.labmanager.infra.notifier.EventBus;
import com.myorg.labmanager.infra.notifier.LogNotifier;
import com.myorg.labmanager.infrastructure.adapter.CsvToEquipoAdapter;
import com.myorg.labmanager.infrastructure.adapter.DataImporter;
import com.myorg.labmanager.infrastructure.facade.ReporteFacade;
import com.myorg.labmanager.infrastructure.persistence.JpaEquipoRepository;
import com.myorg.labmanager.infrastructure.persistence.JpaPrestamoRepository;
import com.myorg.labmanager.infrastructure.persistence.JpaUsuarioRepository;
import com.myorg.labmanager.persistence.repository.EquipoRepository;
import com.myorg.labmanager.persistence.repository.PrestamoRepository;
import com.myorg.labmanager.persistence.repository.UsuarioRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Container {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    
    private final EquipoRepository equipoRepository;
    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    
    private final ReglaPrestamoStrategy regla;
    private final EventBus eventBus;
    
    private final PrestamoService prestamoService;
    private final EquipoService equipoService;
    private final UsuarioService usuarioService;
    
    private final ReporteFacade reporteFacade;
    private final DataImporter csvImporter;

    private Container() {
        // Configurar JPA con H2
        this.emf = Persistence.createEntityManagerFactory("labmanager-pu");
        this.em = emf.createEntityManager();
        
        // Repositorios con JPA
        this.equipoRepository = new JpaEquipoRepository(em);
        this.prestamoRepository = new JpaPrestamoRepository(em);
        this.usuarioRepository = new JpaUsuarioRepository(em);
        
        // Estrategia y Observer
        this.regla = new LimitePorUsuarioStrategy(3);
        this.eventBus = new EventBus();
        this.eventBus.register(new LogNotifier());
        //this.eventBus.register(new EmailNotifierDecorator(new ConsoleNotifier()));
        
        // Servicios
        this.prestamoService = new PrestamoService(equipoRepository, prestamoRepository, regla, eventBus);
        this.equipoService = new EquipoService(equipoRepository, eventBus, prestamoRepository);
        this.usuarioService = new UsuarioService(usuarioRepository, prestamoRepository);
        
        // Facade para reportes
        this.reporteFacade = new ReporteFacade(equipoService, prestamoService);
        
        // Adapter para CSV
        this.csvImporter = new CsvToEquipoAdapter();
        
        seedData();
    }

    private void seedData() {
        // Solo sembrar datos si no hay usuarios
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario(0, "Juan Pérez", "estudiante"));
            usuarioRepository.save(new Usuario(0, "María García", "profesor"));
            usuarioRepository.save(new Usuario(0, "Carlos López", "estudiante"));            
        }
        // Sembrar equipos solo si no hay equipos
        if (equipoRepository.count() == 0) {
            equipoRepository.save(new Equipo(0,"Laptop", "Laptop Dell XPS 15", 5));
            equipoRepository.save(new Equipo(0,"Microscopio", "Microscopio Óptico Olympus", 2));
            equipoRepository.save(new Equipo(0,"Proyector", "Proyector Epson PowerLite", 3));
            equipoRepository.save(new Equipo(0,"Arduino", "Arduino Uno R3", 10));
        }
    }

    public static Container bootstrap() { 
        return new Container(); 
    }
    
    public void shutdown() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    // Getters
    public EquipoService getEquipoService() { return equipoService; }
    public PrestamoService getPrestamoService() { return prestamoService; }
    public UsuarioService getUsuarioService() { return usuarioService; }
    public EventBus getEventBus() { return eventBus; }
    public ReporteFacade getReporteFacade() { return reporteFacade; }
    public DataImporter getCsvImporter() { return csvImporter; }
}
