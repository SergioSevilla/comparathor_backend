package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Estado;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Categoria> findById(Long id);

    List<Producto> findAll();

    List<Producto> findByUsuario(Usuario usuario);

    List<Producto> findByEstado(Estado estado);

    List<Producto> findByUsuarioOrEstado(Usuario usuario, Estado estado);

    List<Producto> findByUsuarioAndEstado(Usuario usuario, Estado estado);
}
