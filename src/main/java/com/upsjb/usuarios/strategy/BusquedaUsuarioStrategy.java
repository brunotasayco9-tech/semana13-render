package com.upsjb.usuarios.strategy;

import com.upsjb.usuarios.model.Usuario;
import java.util.List;

/** Contrato Strategy: cada estrategia define una forma distinta de filtrar usuarios. */
public interface BusquedaUsuarioStrategy {
    List<Usuario> ejecutar();
    String getNombre();
}
