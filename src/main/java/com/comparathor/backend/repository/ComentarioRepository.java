package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Comentario;
import com.comparathor.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComentarioRepository  extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByProducto(Producto producto);

    List<Comentario> findByProductoAndDeletedAtNull(Producto producto);

    Optional<Comentario> findByIdAndDeletedAtNull(int id);
}
