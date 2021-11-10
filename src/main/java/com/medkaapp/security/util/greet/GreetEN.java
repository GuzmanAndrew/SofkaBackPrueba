package com.medkaapp.security.util.greet;

import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.repository.IUsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GreetEN implements IGreet {

    @Autowired
    IUsuarioDao usuarioRepository;

    @Override
    public String greet(String nombreUsuario) {
        /*Usuario usuario = usuarioRepository.findById(id).get();*/
        Optional<Usuario> user = usuarioRepository.findByNombreUsuario(nombreUsuario);
        return "Hello " + user.get().getNombres() + " " + user.get().getApellidos();
    }
}
