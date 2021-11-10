package com.medkaapp.security.util.greet;

import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.repository.IUsuarioDao;
import com.medkaapp.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GreetES implements IGreet {

    @Autowired
    IUsuarioDao usuarioRepository;

    @Override
    public String greet(String nombreUsuario) {
        /*Usuario usuario = usuarioRepository.findById(id).get();*/
        Optional<Usuario> user = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return "Hola " + user.get().getNombres() + " " + user.get().getApellidos();
    }

}
