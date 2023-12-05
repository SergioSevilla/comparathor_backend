package com.comparathor.backend.service;

import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Usuario> userDetail = repository.findByEmail(email);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }

    public String addUser(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        usuario.setCreated_at(new Date(System.currentTimeMillis()));
        usuario.setUpdated_at(new Date(System.currentTimeMillis()));
        repository.save(usuario);
        return "User Added Successfully";
    }

    public String changePassword(String email, String password) {
        Optional<Usuario> oUsuario = repository.findByEmail(email);
        if (oUsuario.isPresent()) {
            Usuario usuario = oUsuario.get();
            usuario.setPassword(encoder.encode(password));
            usuario.setUpdated_at(new Date(System.currentTimeMillis()));
            repository.save(usuario);
            return "User Update Successfully";
        }
        else {
            return "error";
        }


    }


    public String changeUser(String username, Usuario usuario) {
        Optional<Usuario> oUsuario = repository.findByEmail(usuario.getEmail());
        if (oUsuario.isPresent()) {

            return "User Update Successfully";
        }
        else {
            return "error";
        }
    }
}