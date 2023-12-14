package com.comparathor.backend.service;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.CategoriaRepository;
import com.comparathor.backend.repository.RolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> getAllCategorias(Long parentId) {

        if (parentId == null)
        {
            return repository.findAll();
        } else if (parentId == 0) {
            return repository.findByParentId(null);
        } else {
            return repository.findByParentId(parentId);
        }


    }

    public Categoria getCategoria(int id) {

        Optional<Categoria> optCategoria = repository.findById(id);
        if (optCategoria.isPresent())
        {
            return optCategoria.get();
        }
        else
        {
            throw new NoSuchElementFoundException("La categoría "+id+" no existe en el sistema");
        }

    }

    public Categoria modifyCategoria(int id, Categoria categoria) {
        if ((categoryParentExist(categoria.getParentId())) || (categoria.getParentId() == null))
        {
            Optional<Categoria> optCategoria= repository.findById(id);
            if (optCategoria.isPresent())
            {
                Categoria oldCategoria = optCategoria.get();
                oldCategoria.setNombre(categoria.getNombre());
                oldCategoria.setParentId(categoria.getParentId());
                oldCategoria.setUpdated_at(new Date(System.currentTimeMillis()));
                repository.save(oldCategoria);
                return oldCategoria;
            }
            else
            {
                throw new NoSuchElementFoundException("La categoría "+id+" no existe en el sistema");
            }
        }
        else
        {
            throw new NoSuchElementFoundException("La categoría padre no existe en el sistema");
        }


    }

    private boolean categoryParentExist(Long parentId)
    {
        if (repository.findById(parentId).isPresent()) return true; else return false;
    }

    public Categoria addCategory(Categoria categoria) {
        if ((categoryParentExist(categoria.getParentId())) || (categoria.getParentId() == null))
        {
            Categoria categoriaNew = new Categoria();
            categoriaNew.setParentId(categoria.getParentId());
            categoriaNew.setNombre(categoria.getNombre());
            categoriaNew.setCreated_at(new Date(System.currentTimeMillis()));
            categoriaNew.setUpdated_at(new Date(System.currentTimeMillis()));
            repository.save(categoriaNew);
            return categoriaNew;
        }
        else
        {
            throw new NoSuchElementFoundException("La categoría padre no existe en el sistema");
        }
    }
}
