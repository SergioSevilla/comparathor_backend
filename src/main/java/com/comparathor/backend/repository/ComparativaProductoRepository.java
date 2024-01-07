package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Comparativa;
import com.comparathor.backend.entity.ComparativaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparativaProductoRepository extends JpaRepository<ComparativaProducto,Integer> {
    
    List<ComparativaProducto> findByComparativaId(int id);


    void deleteByComparativa(Comparativa comparativa);

    void deleteByComparativaId(Long id);
}
