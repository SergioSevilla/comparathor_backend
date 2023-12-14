package com.comparathor.backend.controller;


import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.ProductoService;
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
@Tag(name = "Servicios de PRODUCTOS", description = "Servicios para gestionar PRODUCTOS")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/item")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Producto> getProductos(@RequestParam(required = false) Long estadoId) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.getAllProductos(user, estadoId);
    }


    @GetMapping("/item/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Producto getProducto(@PathVariable("id") int id ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.getProducto(user, id);
    }

    @PostMapping("/category/{id}/item")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Producto addNewCategoryItem(@PathVariable("id") int categoriaId, @RequestBody Producto producto) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.addProducto(user,producto,categoriaId);
    }

    @PutMapping("/item/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Producto ModifyCategoria(@PathVariable("id") int id, @RequestBody Producto producto) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            return productoService.modifyProductoAdmin(id, producto);
        else
            return productoService.modifyProducto(id, producto, user);
    }

}
