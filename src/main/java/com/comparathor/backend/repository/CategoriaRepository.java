package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository  extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findById(Long id);
    List<Categoria> findByParentId(Long parentId);

    List<Categoria> findAll();

    List<Categoria> findByDeletedAtNull();

    List<Categoria> findByParentIdAndDeletedAtNull(Long parentId);

    Optional<Categoria> findByIdAndDeletedAtNull(int id);
}
