package com.comparathor.backend.controller;

import com.comparathor.backend.entity.Origen;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.OrigenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Servicios de PROVEEDORES", description = "Servicios para gestionar PROVEEDORES")
public class OrigenController {

    @Autowired
    private OrigenService origenService;

    @GetMapping("/suppliers")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Origen> getSuppliers() {
        return origenService.getOrigenes();
    }

    @GetMapping("/suppliers/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Origen getSupplier(@PathVariable("id") int id) {
        return origenService.getOrigen(id);
    }

    @PostMapping("/suppliers")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public Origen addSupplier(@RequestBody Origen origen) {
        return origenService.addOrigen(origen);
    }

    @PutMapping("/suppliers/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Origen ModifySupplier(@PathVariable("id") int id, @RequestBody Origen origen) {
        return origenService.modifyOrigen(id, origen);
    }
}
