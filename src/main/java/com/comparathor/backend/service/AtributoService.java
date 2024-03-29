package com.comparathor.backend.service;

import com.comparathor.backend.entity.Atributo;
import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.AtributoRepository;
import com.comparathor.backend.repository.CategoriaRepository;
import com.comparathor.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AtributoService {

    @Autowired
    private final AtributoRepository repository;

    @Autowired
    private final CategoriaRepository repositoryCategoria;

    @Autowired
    private final ProductoRepository repositoryProducto;

    public AtributoService(AtributoRepository repository, CategoriaRepository repositoryCategoria, ProductoRepository repositoryProducto) {
        this.repository = repository;
        this.repositoryCategoria = repositoryCategoria;
        this.repositoryProducto = repositoryProducto;
    }

    public List<Atributo> getAtributos(UserInfoDetails user) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            return repository.findAll();
        }
        else
        {
            return repository.findByDeletedAtNull();
        }
    }

    public List<Atributo> getAtributosCategoria(int id, UserInfoDetails user) {
        Optional<Categoria> categoriaOptional;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            categoriaOptional = repositoryCategoria.findById(id);
        }
        else
        {
            categoriaOptional = repositoryCategoria.findByIdAndDeletedAtNull(id);
        }
        if (categoriaOptional.isPresent()) {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                return repository.findByCategoria(categoriaOptional.get());
            }
            else
            {
                return repository.findByCategoriaAndDeletedAtNull(categoriaOptional.get());
            }

        } else
            throw new NoSuchElementFoundException("La categoría no existe en el sistema");

    }

    public List<Atributo> getAtributosProducto(int id, UserInfoDetails user) {
        Optional<Producto> productoOptional;
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            productoOptional = repositoryProducto.findById(id);
        }
        else
        {
            productoOptional = repositoryProducto.findByIdAndDeletedAtNull(id);
        }
        if (productoOptional.isPresent()) {
            if ((user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) ||
                    (productoOptional.get().getEstado() == 2) ||
                    ((productoOptional.get().getEstado() == 1) && (user.getUsername().equals(productoOptional.get().getObjectUsuario().getEmail()))))
            {
                List<Categoria> familiaCategorias = getAllFamilyCategories(productoOptional.get().getCategoria(),user);
                if (!(user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
                    familiaCategorias.removeIf((categoria) -> (categoria.getDeleted_at() != null));
                List<Atributo> familiaAtributos = getAllPropertiesItem(familiaCategorias);
                if (!(user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
                    familiaAtributos.removeIf((atributo) -> (atributo.getDeleted_at() != null));
                return familiaAtributos;
            }
            else
                throw new NoSuchElementFoundException("El producto aún no se encuentra disponible en el sistema");
        } else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    private List<Atributo> getAllPropertiesItem(List<Categoria> familiaCategorias) {
        List<Atributo> atributos = new ArrayList<>();
        familiaCategorias.forEach((categoria) -> atributos.addAll(repository.findByCategoria(categoria)));
        return atributos;
    }

    private List<Categoria> getAllFamilyCategories(Long id, UserInfoDetails user) {
        List<Categoria> categorias = new ArrayList<>();
        Categoria categoria = repositoryCategoria.findById(id).get();
        categorias.add(categoria);
        while (categoria.getParentId() != null) {
            categoria = repositoryCategoria.findById(categoria.getParentId()).get();
            categorias.add(categoria);
        }
        return categorias;
    }

    public Atributo addPropiedadCategoria(int id, Atributo atributo) {
        Optional<Categoria> categoriaOptional = repositoryCategoria.findById(id);
        if (categoriaOptional.isPresent()) {
            Atributo atributoToAdd = new Atributo();
            atributoToAdd.setCategoria(categoriaOptional.get());
            atributoToAdd.setNombre(atributo.getNombre());
            atributoToAdd.setCreated_at(new Date(System.currentTimeMillis()));
            atributoToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
            repository.save(atributoToAdd);
            return atributoToAdd;
        } else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    public Atributo modifyPropiedadCategoria(int id, Atributo atributo) {
        Optional<Atributo> atributoOptional = repository.findById(id);
        if (atributoOptional.isPresent()) {
            Atributo atributoToModify = atributoOptional.get();
            atributoToModify.setNombre(atributo.getNombre());
            atributoToModify.setCategoria(repositoryCategoria.findById(atributoOptional.get().getCategoria()).get());
            atributoToModify.setUpdated_at(new Date(System.currentTimeMillis()));
            repository.save(atributoToModify);
            return atributoToModify;

        } else
            throw new NoSuchElementFoundException("El atrubuto no existe en el sistema");
    }

    public Atributo deleteAtributo(int id) {

        Optional<Atributo> atributoOptional = repository.findByIdAndDeletedAtNull(id);
        if (atributoOptional.isPresent())
        {
            repository.delete(atributoOptional.get());
            return null;
            // todo : hard delete de valores!
        }
        else
            throw new NoSuchElementFoundException("El atributo no se encuentra en el sistema");
    }
}
