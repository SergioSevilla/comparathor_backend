package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.service.CategoriaService;
import com.comparathor.backend.service.JwtService;
import com.comparathor.backend.service.UserInfoDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de CATEGORIA", description = "Servicios para gestionar categorias de productos")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping("/categories")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Categoria> getCategorias(@RequestParam(required = false) Long parentId) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return categoriaService.getAllCategorias(parentId,user);
    }



    @GetMapping("/categories/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Categoria getCategoria(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return categoriaService.getCategoria(id,user);
    }

    @PostMapping("/categories")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public Categoria addNewCategory(@RequestBody Categoria categoria) {

        return categoriaService.addCategory(categoria);
    }

    @PutMapping("/categories/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Categoria ModifyCategoria(@PathVariable("id") int id, @RequestBody Categoria categoria) {

        return categoriaService.modifyCategoria(id, categoria);
    }

    @DeleteMapping("/categories/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Categoria deleteCategoria(@PathVariable("id") int id) {

        return categoriaService.deleteCategoria(id);
    }

}
