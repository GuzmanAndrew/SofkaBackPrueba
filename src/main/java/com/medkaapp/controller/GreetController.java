package com.medkaapp.controller;

import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GreetController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/listUsers")
    public List<Usuario> UsuarioList() {
        return usuarioService.getListUsuarios();
    }

}
