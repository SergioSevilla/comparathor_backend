package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Comentario;
import com.comparathor.backend.entity.Puntuacion;
import com.comparathor.backend.service.PuntuacionService;
import com.comparathor.backend.service.UserInfoDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de PUNTUACIONES", description = "Servicios para gestionar PUNTUACIONES")
public class PuntuacionController {

    @Autowired
    private PuntuacionService puntuacionService;

    @GetMapping("/items/{id}/scores")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Puntuacion> getPuntuacion(@PathVariable("id") int id ) {
        return puntuacionService.getScoresItem(id);
    }

    @PutMapping("/scores/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Puntuacion ModifyPuntuacion(@PathVariable("id") int id, @RequestBody Puntuacion puntuacion ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return puntuacionService.modifyPuntuacion(id,puntuacion, user);
    }

    @PostMapping("/items/{id}/scores")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Puntuacion addPuntuacion(@PathVariable("id") int id, @RequestBody Puntuacion puntuacion ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return puntuacionService.addPuntuacion(id,puntuacion,user);

    }
}
