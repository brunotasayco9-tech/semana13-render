package com.upsjb.usuarios.repository;

import com.upsjb.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    List<Usuario> findByEdadGreaterThan(Integer edad);
    boolean existsByEmailIgnoreCase(String email);
}
