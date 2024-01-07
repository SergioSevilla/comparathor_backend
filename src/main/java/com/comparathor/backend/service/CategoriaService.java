package com.comparathor.backend.service;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.CategoriaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> getAllCategorias(Long parentId, UserInfoDetails user) {

        if (parentId == null)
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
                return repository.findAll();
            else
                return repository.findByDeletedAtNull();
        } else if (parentId == 0) {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
                return repository.findByParentId(null);
            else
                return repository.findByParentIdAndDeletedAtNull(null);

        } else {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
                return repository.findByParentId(parentId);
            else
                return repository.findByParentIdAndDeletedAtNull(parentId);

        }


    }

    public Categoria getCategoria(int id, UserInfoDetails user) {
        Optional<Categoria> optCategoria;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            optCategoria = repository.findById(id);
        else
            optCategoria = repository.findByIdAndDeletedAtNull(id);
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
        Optional<Categoria> categoriaOptional = repository.findById(id);
        if (categoriaOptional.isPresent())
        {
            if (categoryParentExist(categoria.getParentId()))
            {
                Categoria oldCategoria = categoriaOptional.get();
                oldCategoria.setNombre(categoria.getNombre());
                oldCategoria.setParentId(categoria.getParentId());
                oldCategoria.setUpdated_at(new Date(System.currentTimeMillis()));
                repository.save(oldCategoria);
                return oldCategoria;
            }
            else
            {
                throw new NoSuchElementFoundException("La categoría padre no existe en el sistema");
            }
        }
        else
            throw new NoSuchElementFoundException("La categoría "+id+" no existe en el sistema");
    }

    private boolean categoryParentExist(Long parentId)
    {
        if (parentId == null)
        {
            return true;
        }
        else {
            if (repository.findByIdAndDeletedAtNull(Math.toIntExact(parentId)).isPresent()) return true;
            else return false;
        }
    }

    public Categoria addCategory(Categoria categoria) {
        if (categoryParentExist(categoria.getParentId()))
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

    public Categoria deleteCategoria(int id) {


        Optional<Categoria> categoriaOptional = repository.findById(id);
       if (categoriaOptional.isPresent())
       {
           List<Categoria> categoriasToDelete = getAllFamily (repository.findById(id).get());
           for(Categoria categoria : categoriasToDelete)
           {
               repository.delete(categoria);
           }

       }
       else
           throw new NoSuchElementFoundException("No existe la categoria en el sistema");
       return null;
    }

    public List<Categoria> getAllFamily (Categoria categoria)
    {
        if (categoria == null)
        {
            return null;
        }
        else
        {
            List<Categoria> lista = new ArrayList<>();
            lista.add(categoria);
            List<Categoria> categorias = repository.findByParentId(categoria.getId());
            for(Categoria cat : categorias)
            {
                lista.addAll(getAllFamily(cat));
            }
            return lista;
        }
    }
}
