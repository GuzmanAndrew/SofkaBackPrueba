package com.medkaapp.security.controller;

import com.medkaapp.security.dto.JwtDto;
import com.medkaapp.security.dto.LoginUsuario;
import com.medkaapp.security.dto.NuevoUsuario;
import com.medkaapp.security.entity.Rol;
import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.enums.RolNombre;
import com.medkaapp.security.jwt.JwtProvider;
import com.medkaapp.security.service.RolService;
import com.medkaapp.security.service.UsuarioService;
import com.medkaapp.security.util.greet.GreetEN;
import com.medkaapp.security.util.greet.GreetES;
import com.medkaapp.security.util.greet.GreetFR;
import com.medkaapp.util.mensajes.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    GreetES greetES;

    @Autowired
    GreetEN greetEN;

    @Autowired
    GreetFR greetFR;

    @GetMapping("/greet/es/{usuario}")
    public ResponseEntity<?> greetEs(@PathVariable(name = "usuario") String usuario) {
        return new ResponseEntity(new Mensaje(greetES.greet(usuario)), HttpStatus.CREATED);
    }

    @GetMapping("/greet/en/{usuario}")
    public ResponseEntity<?> greetEn(@PathVariable(name = "usuario") String usuario) {
        return new ResponseEntity(new Mensaje(greetEN.greet(usuario)), HttpStatus.CREATED);
    }

    @GetMapping("/greet/fr/{usuario}")
    public ResponseEntity<?> greetFr(@PathVariable(name = "usuario") String usuario) {
        return new ResponseEntity(new Mensaje(greetFR.greet(usuario)), HttpStatus.CREATED);
    }

    @GetMapping("/findUsuario/{usuario}")
    public Usuario PacienteId(@PathVariable(name = "usuario") String usuario) {
        return usuarioService.getByNombreUsuario(usuario).get();
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos o email inv??lido"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        Usuario usuario =
                new Usuario(nuevoUsuario.getNombres(), nuevoUsuario.getApellidos(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        nuevoUsuario.getEdad(), nuevoUsuario.getCedula(), nuevoUsuario.getDireccion(), nuevoUsuario.getCelular(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        if(nuevoUsuario.getRoles().contains("admin")) {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        } else {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        }
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
}
