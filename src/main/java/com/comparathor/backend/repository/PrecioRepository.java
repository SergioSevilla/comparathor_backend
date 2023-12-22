package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Origen;
import com.comparathor.backend.entity.Precio;
import com.comparathor.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrecioRepository extends JpaRepository<Precio,Integer> {
    List<Precio> findByProducto(Producto producto);

    Optional<Precio> findByProductoAndOrigen(Producto producto, Origen origen);
}
