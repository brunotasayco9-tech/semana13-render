
package com.upsjb.usuarios.controller;

import com.upsjb.usuarios.model.Usuario;
import com.upsjb.usuarios.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // LISTAR Y BUSCAR USUARIOS POR EMAIL
    @GetMapping
    public String inicio(
            @RequestParam(required = false) String email,
            Model model) {

        model.addAttribute("usuario", new Usuario());

        model.addAttribute("usuarios",
                email == null || email.isBlank()
                        ? service.listar()
                        : service.buscarPorEmail(email)
                            .map(java.util.List::of)
                            .orElse(java.util.List.of()));

        model.addAttribute("filtroEmail",
                email == null ? "" : email);

        model.addAttribute("titulo", "Gestión de usuarios");

        return "usuarios";
    }

    // REGISTRAR USUARIO
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes flash,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuarios", service.listar());
            model.addAttribute("filtroEmail", "");
            model.addAttribute("titulo", "Gestión de usuarios");

            return "usuarios";
        }

        try {
            service.guardar(usuario);

            flash.addFlashAttribute(
                    "exito",
                    "Usuario registrado correctamente.");

        } catch (IllegalArgumentException e) {

            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // EDITAR USUARIO
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model,
            RedirectAttributes flash) {

        return service.buscarPorId(id).map(usuario -> {

            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarios", service.listar());
            model.addAttribute("filtroEmail", "");
            model.addAttribute("titulo", "Editar usuario");

            return "usuarios";

        }).orElseGet(() -> {

            flash.addFlashAttribute(
                    "error",
                    "Usuario no encontrado.");

            return "redirect:/usuarios";
        });
    }

    // ACTUALIZAR USUARIO
    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes flash,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuarios", service.listar());
            model.addAttribute("filtroEmail", "");
            model.addAttribute("titulo", "Editar usuario");

            return "usuarios";
        }

        try {
            service.actualizar(id, usuario);

            flash.addFlashAttribute(
                    "exito",
                    "Usuario actualizado correctamente.");

        } catch (IllegalArgumentException e) {

            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // ELIMINAR USUARIO
    @PostMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes flash) {

        try {
            service.eliminar(id);

            flash.addFlashAttribute(
                    "exito",
                    "Usuario eliminado correctamente.");

        } catch (IllegalArgumentException e) {

            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuarios";
    }

    // CONSULTAR MAYORES DE 18 UTILIZANDO STRATEGY
    @GetMapping("/mayores-18")
    public String mayores(Model model) {

        model.addAttribute("usuario", new Usuario());

        model.addAttribute(
                "usuarios",
                service.ejecutarEstrategia("mayoresDe18")
        );

        model.addAttribute("filtroEmail", "");

        model.addAttribute(
                "titulo",
                "Usuarios mayores de 18 años"
        );

        return "usuarios";
    }

    // EJECUTAR ESTRATEGIAS DINÁMICAMENTE
    @GetMapping("/estrategia/{clave}")
    public String estrategia(
            @PathVariable String clave,
            Model model) {

        model.addAttribute("usuario", new Usuario());

        model.addAttribute(
                "usuarios",
                service.ejecutarEstrategia(clave)
        );

        model.addAttribute("filtroEmail", "");

        model.addAttribute(
                "titulo",
                "Resultado de estrategia"
        );

        return "usuarios";
    }
}
