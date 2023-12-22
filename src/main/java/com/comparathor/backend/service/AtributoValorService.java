package com.comparathor.backend.service;

import com.comparathor.backend.entity.Atributo;
import com.comparathor.backend.entity.AtributoValor;
import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.AtributoRepository;
import com.comparathor.backend.repository.AtributoValorRepository;
import com.comparathor.backend.repository.CategoriaRepository;
import com.comparathor.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AtributoValorService {

    @Autowired
    private final AtributoValorRepository repository;

    @Autowired
    private final AtributoRepository repositoryAtributo;

    @Autowired
    private final ProductoRepository repositoryProducto;

    @Autowired
    private final CategoriaRepository repositoryCategoria;



    public AtributoValorService(AtributoValorRepository repository, AtributoRepository repositoryAtributo,
                                ProductoRepository repositoryProducto, CategoriaRepository repositoryCategoria ) {
        this.repository = repository;
        this.repositoryAtributo = repositoryAtributo;
        this.repositoryProducto = repositoryProducto;
        this.repositoryCategoria = repositoryCategoria;
    }

    public List<AtributoValor> getAtributoValor(int id, UserInfoDetails user) {
        Optional<Producto> productoOptional = repositoryProducto.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                return repository.findByProducto(producto);
            }
            else
            {
                if ((producto.getEstado() == 2) ||
                        ((producto.getEstado() == 1) && (producto.getObjectUsuario().getEmail().equals(user.getUsername()))))
                {
                    return repository.findByProducto(producto);
                }
                else
                    throw new NoSuchElementFoundException("El producto aún no está disponible");
            }
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");
      }

    public AtributoValor addAtributoValor(UserInfoDetails user, AtributoValor atributoValor) {
        if (checkAtributoValor(atributoValor))
        {
            Producto producto = repositoryProducto.findById(atributoValor.getProductoObject().getId()).get();
            Atributo atributo = repositoryAtributo.findById(atributoValor.getAtributoObject().getId()).get();
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                AtributoValor atributoValorToAdd = new AtributoValor();
                atributoValorToAdd.setAtributo(atributo);
                atributoValorToAdd.setProducto(producto);
                atributoValorToAdd.setValor(atributoValor.getValor());
                atributoValorToAdd.setCreated_at(new Date(System.currentTimeMillis()));
                atributoValorToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
                repository.save(atributoValorToAdd);
                return atributoValorToAdd;
            }
            else {
                if ((producto.getEstado()==1) && (user.getUsername().equals(producto.getObjectUsuario().getEmail())))
                {
                    AtributoValor atributoValorToAdd = new AtributoValor();
                    atributoValorToAdd.setAtributo(atributo);
                    atributoValorToAdd.setProducto(producto);
                    atributoValorToAdd.setValor(atributoValor.getValor());
                    atributoValorToAdd.setCreated_at(new Date(System.currentTimeMillis()));
                    atributoValorToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
                    repository.save(atributoValorToAdd);
                    return atributoValorToAdd;
                }
                else
                    throw new NoSuchElementFoundException("El producto no pertenece al usuario o el producto está en estado definitivo");
            }
        }
        else
            throw new NoSuchElementFoundException("Error al añadir valor al atributo");
    }

    private boolean checkAtributoValor(AtributoValor atributoValor) {
        Optional<Producto> productoOptional = repositoryProducto.findById(atributoValor.getProductoObject().getId());
        Optional<Atributo> atributoOptional = repositoryAtributo.findById(atributoValor.getAtributoObject().getId());
        if (!(productoOptional.isPresent()))
        {
            throw  new NoSuchElementFoundException("El producto no existe en el sistema");
        }
        else if (!(atributoOptional.isPresent()))
        {
            throw  new NoSuchElementFoundException("El atributo no existe en el sistema");
        }
        else
        {
            List<Categoria> categorias = getAllFamilyCategories(productoOptional.get().getCategoria());
            if (!(categorias.contains(atributoOptional.get().getCategoriaObj())))
            {
                throw  new NoSuchElementFoundException("El producto no contiene el atributo");
            }
            else return true;
        }
    }

    private List<Categoria> getAllFamilyCategories(Long id) {
        List<Categoria> categorias = new ArrayList<>();
        Categoria categoria = repositoryCategoria.findById(id).get();
        categorias.add(categoria);
        while (categoria.getParentId() != null) {
            categoria = repositoryCategoria.findById(categoria.getParentId()).get();
            categorias.add(categoria);
        }
        return categorias;
    }

    public AtributoValor modifyAtributoValor(int id, UserInfoDetails user, AtributoValor atributoValor) {
        Optional<AtributoValor> atributoValorOptional = repository.findById(id);
        if (atributoValorOptional.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                AtributoValor atributoToMod = atributoValorOptional.get();
                atributoToMod.setValor(atributoValor.getValor());
                atributoToMod.setCreated_at(new Date(System.currentTimeMillis()));
                atributoToMod.setUpdated_at(new Date(System.currentTimeMillis()));
                if (atributoValor.getProducto() != null)
                {
                    Optional<Producto> productoOptional = repositoryProducto.findById(atributoValor.getProducto());
                    if (productoOptional.isPresent())
                    {
                        atributoToMod.setProducto(productoOptional.get());
                    }
                    else
                        throw new NoSuchElementFoundException("El producto "+atributoValor.getProducto()+" no existe");
                }
                if (atributoValor.getAtributo() != null)
                {
                    Optional<Atributo> atributoOptional = repositoryAtributo.findById(atributoValor.getAtributo());
                    if (atributoOptional.isPresent())
                    {
                        atributoToMod.setAtributo(atributoOptional.get());
                    }
                    else
                        throw new NoSuchElementFoundException("El atributo "+atributoValor.getAtributo()+" no existe");
                }
            }
            else {

            }
        }
        else
            throw new NoSuchElementFoundException("El valor no existe en el sistema");
    }
}
