package com.upsjb.usuarios.strategy;

import com.upsjb.usuarios.model.Usuario;
import com.upsjb.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("mayoresDe18")
public class MayoresDe18Strategy implements BusquedaUsuarioStrategy {
    private final UsuarioRepository repository;
    public MayoresDe18Strategy(UsuarioRepository repository) { this.repository = repository; }
    @Override public List<Usuario> ejecutar() { return repository.findByEdadGreaterThan(18); }
    @Override public String getNombre() { return "Usuarios mayores de 18 años"; }
}
