package com.comparathor.backend.controller;


import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.ProductoService;
import com.comparathor.backend.service.UserInfoDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de PRODUCTOS", description = "Servicios para gestionar PRODUCTOS")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Producto> getProductos(@RequestParam(required = false) Long estadoId) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.getAllProductos(user, estadoId);
    }


    @GetMapping("/items/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Producto getProducto(@PathVariable("id") int id ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.getProducto(user, id);
    }

    @PostMapping("/categories/{id}/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public Producto addNewCategoryItem(@PathVariable("id") int categoriaId, @RequestBody Producto producto) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.addProducto(user,producto,categoriaId);
    }


    @GetMapping("/categories/{id}/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Producto>  getProductoByCat(@PathVariable("id") int categoriaId) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.getProductoByCat(user, categoriaId);
    }

    @PutMapping("/items/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Producto ModifyProducto(@PathVariable("id") int id, @RequestBody Producto producto) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            return productoService.modifyProductoAdmin(id, producto);
        else
            return productoService.modifyProducto(id, producto, user);
    }

    @PostMapping("/items/{id}/picture")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @ResponseStatus( HttpStatus.CREATED)
    public Producto addPicture(@PathVariable("id") int id, @RequestParam("image") MultipartFile multipartFile) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return productoService.saveImage(multipartFile, id, user);
    }

    @GetMapping("/items/{id}/picture")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<?> getPicture(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return productoService.getImage(id, user);
    }

    @DeleteMapping("/items/{id}/picture")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Producto deletePicture(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return productoService.deletePicture(id, user);
    }

    @DeleteMapping("/items/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Producto deleteProducto(@PathVariable("id") int id ) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return productoService.deleteProducto(id,user);
    }


}
