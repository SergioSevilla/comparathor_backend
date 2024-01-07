package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Atributo;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.AtributoService;
import com.comparathor.backend.service.UserInfoDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de ATRIBUTOS", description = "Servicios para gestionar ATRIBUTOS")
public class AtributoController {

    @Autowired
    private AtributoService atributoService;

    @GetMapping("/properties")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Atributo> getProperties() {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoService.getAtributos(user);
    }

    @GetMapping("/categories/{id}/properties")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Atributo> getCategoryProperties(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoService.getAtributosCategoria(id,user);
    }

    @GetMapping("/items/{id}/properties")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Atributo> getItemProperties(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoService.getAtributosProducto(id,user);
    }

    @PostMapping("/categories/{id}/properties")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public Atributo addCategoryProperty(@PathVariable("id") int id, @RequestBody Atributo atributo) {

        return atributoService.addPropiedadCategoria(id,atributo);
    }

    @PutMapping("/properties/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Atributo modifyCategoryProperty(@PathVariable("id") int id, @RequestBody Atributo atributo) {

        return atributoService.modifyPropiedadCategoria(id,atributo);
    }

    @DeleteMapping("/properties/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Atributo deleteProperty(@PathVariable("id") int id) {

        return atributoService.deleteAtributo(id);
    }
}
