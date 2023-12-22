package com.comparathor.backend.controller;



import com.comparathor.backend.entity.Comentario;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.ComentarioService;
import com.comparathor.backend.service.UserInfoDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de COMENTARIOS", description = "Servicios para gestionar COMENTARIOS")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;


    @GetMapping("/items/{id}/comments")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Comentario> getComentarios(@PathVariable("id") int id ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            return comentarioService.getAllCommentsItemAdmin(id);
        }
        else
        {
            return comentarioService.getAllCommentsItem(id,user);
        }

    }

    @PostMapping("/items/{id}/comments")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Comentario addComentario(@PathVariable("id") int id,@RequestBody Comentario comentario ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comentarioService.addComentario(id,user, comentario);

    }

    @PutMapping("/comments/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Comentario modifyComentario(@PathVariable("id") int id,@RequestBody Comentario comentario ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comentarioService.modifyComentario(id,user, comentario);

    }



}
