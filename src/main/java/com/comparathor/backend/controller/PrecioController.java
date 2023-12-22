package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Precio;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.PrecioService;
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
@Tag(name = "Servicios de PRECIOS", description = "Servicios para gestionar PRECIOS")
public class PrecioController {

    @Autowired
    private PrecioService precioService;


    @GetMapping("/items/{id}/prices")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Precio> getItemPrices(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return precioService.getPreciosProducto(user, id);
    }

    @PostMapping("/prices")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Precio addItemPrices( @RequestBody Precio precio) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return precioService.addPreciosProducto(user, precio);
    }

    @PutMapping("/prices/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Precio ModifyPrice(@PathVariable("id") int id,  @RequestBody Precio precio) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return precioService.modifyPrecio(user, id, precio);
    }

}
