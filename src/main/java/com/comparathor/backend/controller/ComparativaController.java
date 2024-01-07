package com.comparathor.backend.controller;


import com.comparathor.backend.entity.Comparativa;
import com.comparathor.backend.entity.ComparativaProducto;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.service.ComparativaService;
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
@Tag(name = "Servicios de COMPARATIVAS", description = "Servicios para gestionar COMPARATIVAS")
public class ComparativaController {


    @Autowired
    private ComparativaService comparativaService;

    @GetMapping("/comparisons")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Comparativa> getComparisons() {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.getComparativas(user);

    }

    @GetMapping("/comparisons/{id}/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<Producto> getComparisonsItems(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.getComparativaProducto(id, user);

    }

    @PostMapping("/comparisons")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public Comparativa addComparison(@RequestBody Comparativa comparativa) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.addComparativa(comparativa, user);

    }



    @PostMapping("/comparisons/{id}/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @ResponseStatus( HttpStatus.CREATED)
    public List<ComparativaProducto> addComparisonItems(@PathVariable("id") int id, @RequestBody List<Producto> productos) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.addComparativaProductos(id, productos,user);

    }

    @PutMapping("/comparisons/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Comparativa modifyComparison(@PathVariable("id") int id, @RequestBody Comparativa comparativa) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.modifyComparativa(id, comparativa,user);

    }

    @DeleteMapping("/comparisons/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Comparativa deleteComparison(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.deleteComparativa(id, user);

    }

    @DeleteMapping("/comparisons/{id}/items")
    @SecurityRequirement(name="Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public Comparativa deleteComparisonItems(@PathVariable("id") int id) {
        UserInfoDetails user = (UserInfoDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return comparativaService.deleteComparativaProductos(id, user);

    }

}
