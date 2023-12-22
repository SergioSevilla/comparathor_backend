package com.comparathor.backend.service;

import com.comparathor.backend.entity.Origen;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.OrigenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrigenService {

    @Autowired
    private final OrigenRepository repository;

    public OrigenService(OrigenRepository repository) {
        this.repository = repository;
    }

    public List<Origen> getOrigenes() {
        return repository.findAll();
    }

    public Origen getOrigen(int id) {
        Optional<Origen> origenOptional= repository.findById(id);
        if (origenOptional.isPresent())
        {
            return origenOptional.get();
        }
        else
            throw new NoSuchElementFoundException("El orígen no existe en el sistema");
    }

    public Origen addOrigen(Origen origen) {
        Origen origenToAdd = new Origen();
        origenToAdd.setNombre(origen.getNombre());
        origenToAdd.setCreated_at(new Date(System.currentTimeMillis()));
        origenToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
        repository.save(origenToAdd);
        return origenToAdd;
    }

    public Origen modifyOrigen(int id, Origen origen) {
        //hay que revisar si el origen existe
        Optional<Origen> origenOptional = repository.findById(id);
        if (origenOptional.isPresent())
        {
            Origen origenToModify = origenOptional.get();
            origenToModify.setNombre(origen.getNombre());
            repository.save(origenToModify);
            return origenToModify;
        }
        else
            throw new NoSuchElementFoundException("El orígen no existe en el sistema");
    }
}
