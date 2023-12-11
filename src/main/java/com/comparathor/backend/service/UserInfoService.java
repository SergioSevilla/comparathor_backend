package com.comparathor.backend.service;

import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public String modifyUser(String username, Usuario usuario) {
        Optional<Usuario> oUsuario = repository.findByEmail(username);
        if (oUsuario.isPresent()) {
            Usuario usuarioOld = oUsuario.get();
            usuarioOld.setDireccion(usuario.getDireccion());
            usuarioOld.setNombre(usuario.getNombre());
            usuarioOld.setUpdated_at(new Date(System.currentTimeMillis()));
            repository.save(usuarioOld);
            return "User Update Successfully";
        }
        else {
            return "error";
        }
    }

    public Usuario getUser(int id) {
        Optional<Usuario> oUsuario= repository.findById(id);
        Usuario usuario = null;
        if (oUsuario.isPresent()) {
            usuario = oUsuario.get();
            usuario.setPassword("");

        }
        return usuario;
    }


    public List<Usuario> getUsers(UserInfoDetails user) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            usuarios = repository.findAll();
            usuarios.stream().forEach(usr -> usr.setPassword(null));
        }
        else
        {
            Optional<Usuario> oUsuario= repository.findByEmail(user.getUsername());
            Usuario usuario = null;
            if (oUsuario.isPresent()) {
                usuario = oUsuario.get();
                usuario.setPassword("");

            }
            usuarios.add(usuario);
        }
        return usuarios;
    }

    public Usuario modifyUser(int id, Usuario usuario)
    {
        Optional<Usuario> oUsuario= repository.findById(id);
        Usuario oldUsuario = null;
        if (oUsuario.isPresent()) {
            oldUsuario = oUsuario.get();
        }
        oldUsuario.setDireccion(usuario.getDireccion());
        oldUsuario.setNombre(usuario.getNombre());
        oldUsuario.setUpdated_at(new Date(System.currentTimeMillis()));
        repository.save(oldUsuario);
        oldUsuario.setPassword("");
        return oldUsuario;
    }

    public String checkEmail(String email)
    {
        Optional<Usuario> oUsuario = repository.findByEmail(email);
        if (oUsuario.isPresent()) {
            return "Existe";
        }
        else {
            return "No existe";
        }
    }
}