package com.comparathor.backend.service;

import com.comparathor.backend.entity.Rol;
import com.comparathor.backend.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;


    public RolService(final RolRepository rolRepository) {
        this.rolRepository = rolRepository;

    }

    public Rol getRolById(final int id) {
        final Rol rol = this.rolRepository.findById(id);
        return rol;
    }

}
