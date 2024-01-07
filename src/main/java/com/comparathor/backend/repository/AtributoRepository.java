package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Atributo;
import com.comparathor.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AtributoRepository extends JpaRepository<Atributo, Integer> {
    List<Atributo> findByCategoria(Categoria categoria);

    Optional<Atributo> findById(Long id);
    
    List<Atributo> findByDeletedAtNull();

    List<Atributo> findByCategoriaAndDeletedAtNull(Categoria categoria);

    Optional<Atributo> findByIdAndDeletedAtNull(int id);
}
