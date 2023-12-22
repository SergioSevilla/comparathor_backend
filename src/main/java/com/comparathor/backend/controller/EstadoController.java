package com.comparathor.backend.controller;


import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Estado;
import com.comparathor.backend.service.EstadoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de ESTADOS", description = "Servicios para gestionar ESTADOS")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @GetMapping("/statuses")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Estado> getEstado() {

        return estadoService.getAllEstados();
    }

    @GetMapping("/statuses/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Estado getCategoria(@PathVariable("id") int id) {

        return estadoService.getEstado(id);
    }

}
