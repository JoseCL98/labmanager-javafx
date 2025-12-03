package com.myorg.labmanager.domain.service;

import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.domain.model.Usuario;
import com.myorg.labmanager.persistence.repository.PrestamoRepository;
import com.myorg.labmanager.persistence.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioRepository repo;
    private final PrestamoRepository prestamoRepo;

    public UsuarioService(UsuarioRepository repo, PrestamoRepository prestamoRepo) {
        this.repo = repo;
        this.prestamoRepo = prestamoRepo;
    }

    public Usuario create(String nombre, String tipo) {
        Usuario usuario = new Usuario(0, nombre, tipo);
        return repo.save(usuario);
    }
    
    public void update(int id, String nombre, String tipo) {
        repo.findById(id).ifPresent(u -> {
            u.setNombre(nombre);
            u.setTipo(tipo);
            repo.update(u);
        });
    }
    
    public boolean delete(int id) {
        // Verificar que no tenga préstamos activos
        List<Prestamo> activos = prestamoRepo.findByUsuarioId(id).stream()
            .filter(p -> "PRESTADO".equals(p.getEstado()))
            .toList();
        if (!activos.isEmpty()) return false;
        repo.delete(id);
        return true;
    }

    public List<Usuario> listAll() {
        return repo.findAll();
    }

    public Optional<Usuario> findById(int id) {
        return repo.findById(id);
    }
    
    public Optional<Usuario> findByNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
    
    public Usuario getOrCreate(String nombre, String tipo) {
        Optional<Usuario> existente = repo.findByNombre(nombre);
        if (existente.isPresent()) {
            return existente.get();
        }
        return create(nombre, tipo);
    }
}
