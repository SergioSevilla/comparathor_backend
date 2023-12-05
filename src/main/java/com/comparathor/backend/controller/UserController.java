package com.comparathor.backend.controller;

import com.comparathor.backend.entity.AuthRequest;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.service.JwtService;
import com.comparathor.backend.service.RolService;
import com.comparathor.backend.service.UserInfoDetails;
import com.comparathor.backend.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private RolService rolService;

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


   @PostMapping("/addNewUser")
   public String addNewUser(@RequestBody Usuario usuario) {
       usuario.setRol(rolService.getRolById(1));
       return service.addUser(usuario);
   }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('USER')")
    public String userProfile() {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
       return "Hello "+user.getUsername();
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminProfile() {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return "Hello "+user.getUsername();
    }

    @PostMapping("/generateToken")
    public String changePassword(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PutMapping("/user/changePassword")
    @PreAuthorize("hasAuthority('USER')")
    public String changePass(@RequestBody Usuario usuario) {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return service.changePassword(user.getUsername(),usuario.getPassword());
    }

    @PutMapping("/user/changeUser")
    @PreAuthorize("hasAuthority('USER')")
    public String changeUser(@RequestBody Usuario usuario) {
        UserInfoDetails user = (UserInfoDetails)SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return service.changeUser(user.getUsername(),usuario);
    }

}