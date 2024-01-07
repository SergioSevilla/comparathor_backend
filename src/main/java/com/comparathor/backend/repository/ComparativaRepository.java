package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Comparativa;
import com.comparathor.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparativaRepository extends JpaRepository<Comparativa, Integer> {

    List<Comparativa> findByUsuario(Usuario usuario);
}
