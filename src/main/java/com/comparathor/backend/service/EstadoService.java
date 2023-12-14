package com.comparathor.backend.service;


import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Estado;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    private final EstadoRepository repository;

    public EstadoService(EstadoRepository repository) {
        this.repository = repository;
    }

    public List<Estado> getAllEstados()
    {
            return repository.findAll();
    }

    public Estado getEstado(long id)
    {

        Optional<Estado> optEstado = repository.findById(id);
        if (optEstado.isPresent())
        {
            return optEstado.get();
        }
        else
        {
            throw new NoSuchElementFoundException("El estado con id="+id+" no existe en el sistema");
        }
    }
}
