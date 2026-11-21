package com.upsjb.usuarios.service;

import com.upsjb.usuarios.model.Usuario;
import com.upsjb.usuarios.repository.UsuarioRepository;
import com.upsjb.usuarios.strategy.BusquedaUsuarioStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final Map<String, BusquedaUsuarioStrategy> estrategias;

    public UsuarioService(UsuarioRepository repository, Map<String, BusquedaUsuarioStrategy> estrategias) {
        this.repository = repository;
        this.estrategias = estrategias;
    }

    public List<Usuario> listar() { return repository.findAll(); }
    public Optional<Usuario> buscarPorId(Long id) { return repository.findById(id); }
    public Optional<Usuario> buscarPorEmail(String email) { return repository.findByEmailIgnoreCase(email); }
    public List<Usuario> mayoresDe18() { return repository.findByEdadGreaterThan(18); }

    @Transactional
    public Usuario guardar(Usuario usuario) {
        if (repository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");
        }
        return repository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario actual = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con ID " + id));
        Optional<Usuario> repetido = repository.findByEmailIgnoreCase(datos.getEmail());
        if (repetido.isPresent() && !repetido.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ese correo ya pertenece a otro usuario.");
        }
        actual.setNombre(datos.getNombre());
        actual.setEmail(datos.getEmail());
        actual.setEdad(datos.getEdad());
        return repository.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("No existe el usuario con ID " + id);
        repository.deleteById(id);
    }

    public List<Usuario> ejecutarEstrategia(String clave) {
        BusquedaUsuarioStrategy estrategia = estrategias.get(clave);
        if (estrategia == null) throw new IllegalArgumentException("Estrategia de búsqueda no válida.");
        return estrategia.ejecutar();
    }
}
