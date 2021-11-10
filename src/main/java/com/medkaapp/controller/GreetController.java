package com.medkaapp.controller;

import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class GreetController {

    @Autowired
    UsuarioService usuarioService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/listUsers")
    public List<Usuario> UsuarioList() {
        return usuarioService.getListUsuarios();
    }

}
