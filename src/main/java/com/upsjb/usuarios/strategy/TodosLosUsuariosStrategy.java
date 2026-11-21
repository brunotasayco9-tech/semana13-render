package com.upsjb.usuarios.strategy;

import com.upsjb.usuarios.model.Usuario;
import com.upsjb.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("todosLosUsuarios")
public class TodosLosUsuariosStrategy implements BusquedaUsuarioStrategy {
    private final UsuarioRepository repository;
    public TodosLosUsuariosStrategy(UsuarioRepository repository) { this.repository = repository; }
    @Override public List<Usuario> ejecutar() { return repository.findAll(); }
    @Override public String getNombre() { return "Todos los usuarios"; }
}
