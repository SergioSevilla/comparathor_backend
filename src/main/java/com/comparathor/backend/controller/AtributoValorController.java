package com.comparathor.backend.controller;


import com.comparathor.backend.entity.Atributo;
import com.comparathor.backend.entity.AtributoValor;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.AtributoValorService;
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
@Tag(name = "Servicios de VALORES", description = "Servicios para gestionar VALORES de un producto")
public class AtributoValorController {

    @Autowired
    private AtributoValorService atributoValorService;


    @GetMapping("/items/{id}/values")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<AtributoValor> getValues(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoValorService.getAtributoValor(id,user);
    }

    @PostMapping("/values")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public AtributoValor addNewCategoryItem(@RequestBody AtributoValor atributoValor) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoValorService.addAtributoValor(user,atributoValor);
    }

    @PutMapping("/values/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public AtributoValor modifyCategoryItem(@PathVariable("id") int id, @RequestBody AtributoValor atributoValor) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return atributoValorService.modifyAtributoValor(id, user,atributoValor);
    }
}
