package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.service.CategoriaService;
import com.comparathor.backend.service.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de CATEGORIA", description = "Servicios para gestionar categorias de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping("/category")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Categoria> getCategorias(@RequestParam(required = false) Long parentId) {

        return categoriaService.getAllCategorias(parentId);
    }



    @GetMapping("/category/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Categoria getCategoria(@PathVariable("id") int id) {

        return categoriaService.getCategoria(id);
    }

    @PostMapping("/category")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Categoria addNewCategory(@RequestBody Categoria categoria) {

        return categoriaService.addCategory(categoria);
    }

    @PutMapping("/category/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Categoria ModifyCategoria(@PathVariable("id") int id, @RequestBody Categoria categoria) {

        return categoriaService.modifyCategoria(id, categoria);
    }



}
