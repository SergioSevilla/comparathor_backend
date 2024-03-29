package com.comparathor.backend.controller;

import com.comparathor.backend.entity.AuthRequest;
import com.comparathor.backend.entity.JwtToken;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.service.JwtService;
import com.comparathor.backend.service.RolService;
import com.comparathor.backend.service.UserInfoDetails;
import com.comparathor.backend.service.UserInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de USUARIO", description = "Servicios para gestionar usuarios y autenticación")
public class UserController {

    @Autowired
    private RolService rolService;

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    // Da de alta un usuario con el rol "USER"
    @PostMapping("/users/create")
    @ResponseStatus( HttpStatus.CREATED)
    public Usuario addNewUser(@RequestBody Usuario usuario) {
        usuario.setRol(rolService.getRolById(1));
        return service.addUser(usuario);
    }


    // Devuelve información del usuario que ejecuta el REST
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public List<Usuario> getUser() {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return service.getUsers(user);
    }

    // Modifica información del usuario que ejecuta el REST
    @PutMapping("/users")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public Usuario ModifyUser(@RequestBody Usuario usuario) {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return service.modifyUser(user.getUsername(), usuario);

    }

    // Devuelve información del usuario ID
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public Usuario getUser(@PathVariable("id") int id) {

        return service.getUser(id);
    }

    // Modifica información del usuario ID
    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public Usuario ModifyUserById(@PathVariable("id") int id, @RequestBody Usuario usuario) {

        return service.modifyUser(id, usuario);
    }

    // Devuelve JWT si el usuario y contraseña es correcta

    @PostMapping("/users/login")
    public JwtToken login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    // Cambia contraseña del usuario logado
    @PutMapping("/users/changePassword")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public Usuario changePass(@RequestBody Usuario usuario) {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return service.changePassword(user.getUsername(),usuario.getPassword());
    }

    //devuelve si el email existe o no
    @GetMapping("/users/checkEmail")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @SecurityRequirement(name="Bearer Authentication")
    public Usuario checkEmail(@RequestParam String email) {

        return service.checkEmail(email);
    }


    //mensaje de bienvenida
    @GetMapping("/users/welcome")
    public String welcomeMessage() {
        return "Welcome Message!";
    }


}