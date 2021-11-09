package com.medkaapp.security.util.greet;

import com.medkaapp.security.entity.Usuario;
import com.medkaapp.security.repository.IUsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GreetEN implements IGreet {

    @Autowired
    IUsuarioDao usuarioRepository;

    @Override
    public String greet(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).get();
        return "Hello " + usuario.getNombres() + " " + usuario.getApellidos();
    }
}
