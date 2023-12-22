package com.comparathor.backend.repository;

import com.comparathor.backend.entity.Origen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenRepository extends JpaRepository<Origen, Integer> {
}
