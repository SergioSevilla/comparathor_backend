package com.comparathor.backend.repository;

import com.comparathor.backend.entity.AtributoValor;
import com.comparathor.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoValorRepository extends JpaRepository<AtributoValor, Integer> {

    List<AtributoValor> findByProducto(Producto producto);
}
