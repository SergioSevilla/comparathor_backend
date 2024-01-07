package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.entity.Puntuacion;
import com.comparathor.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuntuacionRepository  extends JpaRepository<Puntuacion, Integer> {

    List<Puntuacion> findByProducto(Producto producto);

    Optional<Puntuacion> findByProductoAndUsuario(Producto producto, Usuario usuario);

    Optional<Puntuacion> findByIdAndDeletedAtNull(int id);

    List<Puntuacion> findByProductoAndDeletedAtNull(Producto producto);

    Optional<Puntuacion> findByProductoAndUsuarioAndDeletedAtNull(Producto producto, Usuario usuario);
}
