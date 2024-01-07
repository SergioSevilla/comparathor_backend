package com.comparathor.backend.service;

import com.comparathor.backend.entity.*;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ComparativaService {

    private final ComparativaRepository repository;

    private final ComparativaProductoRepository repositoryComparativaProducto;

    private final ProductoRepository repositoryProducto;

    private final UserInfoRepository repositoryUsuario;

    private final CategoriaRepository repositoryCategoria;

    public ComparativaService(ComparativaRepository repository, ComparativaProductoRepository repositoryComparativaProducto,
                              ProductoRepository repositoryProducto, UserInfoRepository repositoryUsuario,
                              CategoriaRepository repositoryCategoria) {
        this.repository = repository;
        this.repositoryComparativaProducto = repositoryComparativaProducto;
        this.repositoryProducto = repositoryProducto;
        this.repositoryUsuario = repositoryUsuario;
        this.repositoryCategoria = repositoryCategoria;
    }

    public List<Comparativa> getComparativas(UserInfoDetails user) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
            return repository.findAll();
        }
        else {
            Usuario usuario = repositoryUsuario.findByEmail(user.getUsername()).get();
            return repository.findByUsuario(usuario);
        }
    }

    public Comparativa addComparativa(Comparativa comparativa, UserInfoDetails user) {
        Comparativa comparativaToAdd = new Comparativa();
        Usuario usuario = repositoryUsuario.findByEmail(user.getUsername()).get();
        comparativaToAdd.setUsuario(usuario);
        comparativaToAdd.setNombre(comparativa.getNombre());
        comparativaToAdd.setCreated_at(new Date(System.currentTimeMillis()));
        comparativaToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
        repository.save(comparativaToAdd);
        return comparativaToAdd;


    }

    public List<ComparativaProducto> addComparativaProductos(int id, List<Producto> productos, UserInfoDetails user) {
        Usuario usuario = repositoryUsuario.findByEmail(user.getUsername()).get();
        Optional<Comparativa> comparativaOptional = repository.findById(id);
        if (comparativaOptional.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                if (checkProductos(productos, false))
                {
                    List<ComparativaProducto> comparativaProductoList = new ArrayList<>();
                    productos.forEach((producto) -> {
                        ComparativaProducto comparativaProductoToADD = new ComparativaProducto();
                        comparativaProductoToADD.setComparativa(comparativaOptional.get());
                        comparativaProductoToADD.setProducto(repositoryProducto.findById(producto.getId()).get());
                        comparativaProductoToADD.setCreated_at(new Date(System.currentTimeMillis()));
                        comparativaProductoToADD.setUpdated_at(new Date(System.currentTimeMillis()));
                        repositoryComparativaProducto.save(comparativaProductoToADD);
                        comparativaProductoList.add(comparativaProductoToADD);
                    });
                    return comparativaProductoList;
                }
                else
                    throw new NoSuchElementFoundException("Error al insertar los productos");
            }
            else
            {
                if (checkProductos(productos, true))
                {
                    List<ComparativaProducto> comparativaProductoList = new ArrayList<>();
                    productos.forEach((producto) -> {
                        ComparativaProducto comparativaProductoToADD = new ComparativaProducto();
                        comparativaProductoToADD.setComparativa(comparativaOptional.get());
                        comparativaProductoToADD.setProducto(repositoryProducto.findById(producto.getId()).get());
                        comparativaProductoToADD.setCreated_at(new Date(System.currentTimeMillis()));
                        comparativaProductoToADD.setUpdated_at(new Date(System.currentTimeMillis()));
                        repositoryComparativaProducto.save(comparativaProductoToADD);
                        comparativaProductoList.add(comparativaProductoToADD);
                    });
                    return comparativaProductoList;
                }
                throw new NoSuchElementFoundException("Error al insertar los productos");
            }
        }
        else
        {
            throw new NoSuchElementFoundException("No se encuentra la comparativa");
        }
    }

    private boolean checkProductos(List<Producto> productos, boolean estado) {

        if (productos.isEmpty()) throw new NoSuchElementFoundException("No hay productos para añadir");
        int rootCat = getRootCategoria(productos.get(0));
        for(Producto producto : productos) {
            if (!repositoryProducto.findById(producto.getId()).isPresent()) throw new NoSuchElementFoundException("Algún producto no existe en el sistema");
            if (rootCat!= getRootCategoria(producto)) throw new NoSuchElementFoundException("Algún producto no coincide con su categoria");
            if (estado)
            {
                if (repositoryProducto.findById(producto.getId()).get().getEstado() == 1) throw new NoSuchElementFoundException("Algún producto aún no está disponible en el sistema");
            }
        }
        return true;
    }

    private int getRootCategoria(Producto producto)
    {
        Producto productoAct = repositoryProducto.findById(producto.getId()).get();
        Long rootCat = productoAct.getCategoria();
        Categoria cat = repositoryCategoria.findById(rootCat).get();
        while(cat.getParentId() != null)
        {
            cat = repositoryCategoria.findById(cat.getParentId()).get();
            rootCat = cat.getId();
        }
        return Math.toIntExact(rootCat);
    }

    public Comparativa modifyComparativa(int id, Comparativa comparativa, UserInfoDetails user) {
        Optional<Comparativa> comparativaOptional = repository.findById(id);
        if (comparativaOptional.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                Comparativa comparativaToMod = comparativaOptional.get();
                comparativaToMod.setNombre(comparativa.getNombre());
                comparativaToMod.setUpdated_at(new Date(System.currentTimeMillis()));
                repository.save(comparativaToMod);
                return comparativaToMod;
            }
            else
            {
                Comparativa comparativaToMod = comparativaOptional.get();
                if (comparativaToMod.getUsuarioObject().getEmail().equals(user.getUsername())) {
                    comparativaToMod.setNombre(comparativa.getNombre());
                    comparativaToMod.setUpdated_at(new Date(System.currentTimeMillis()));
                    repository.save(comparativaToMod);
                    return comparativaToMod;
                }
                else
                    throw new NoSuchElementFoundException("La comparativa no pertenece al usuario");
            }
        }
        else
            throw new NoSuchElementFoundException("La comparativa no existe en el sistema");
    }

    public Comparativa deleteComparativa(int id, UserInfoDetails user) {
        Optional<Comparativa> comparativaOptional = repository.findById(id);
        if (comparativaOptional.isPresent()) {
            if ((user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) ||
                (user.getUsername().equals(comparativaOptional.get().getUsuarioObject().getEmail())))
            {
                List<ComparativaProducto> comparativaProductoList = repositoryComparativaProducto.findByComparativaId(Math.toIntExact(comparativaOptional.get().getId()));
                for (ComparativaProducto cp : comparativaProductoList) repositoryComparativaProducto.delete(cp);
                repository.delete(comparativaOptional.get());
                return null;
            }
            else
                throw new NoSuchElementFoundException("No se puede eliminar una comparativa de otro usuario");

        }
        else
            throw new NoSuchElementFoundException("La comparativa no existe en el sistema");

    }

    public List<Producto> getComparativaProducto(int id, UserInfoDetails user) {
        Optional<Comparativa> comparativaOptional = repository.findById(id);
        if (comparativaOptional.isPresent()) {
            List<ComparativaProducto> comparativaProductos = repositoryComparativaProducto.findByComparativaId(id);
            List<Producto> productos = new ArrayList<>();
            for(ComparativaProducto comparativaProducto : comparativaProductos)
            {
                Producto producto = comparativaProducto.getProducto();
                productos.add(producto);
            }
            if (!(user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
            {
                if (user.getUsername().equals(comparativaOptional.get().getUsuarioObject().getEmail()))
                {
                    productos.removeIf((producto) -> (producto.getDeleted_at() != null));
                    return productos;
                }
                else
                    throw new NoSuchElementFoundException("La comparativa es de otro usuario");
            }
            else
            {
                return productos;
            }

        }
        else
            throw new NoSuchElementFoundException("La comparativa no existe en el sistema");
    }

    public Comparativa deleteComparativaProductos(int id, UserInfoDetails user) {
        Optional<Comparativa> comparativaOptional = repository.findById(id);
        if (comparativaOptional.isPresent()) {
            if ((user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) ||
                    (user.getUsername().equals(comparativaOptional.get().getUsuarioObject().getEmail())))
            {
                List<ComparativaProducto> comparativaProductoList = repositoryComparativaProducto.findByComparativaId(Math.toIntExact(comparativaOptional.get().getId()));
                for (ComparativaProducto cp : comparativaProductoList) repositoryComparativaProducto.delete(cp);
                return null;
            }
            else
                throw new NoSuchElementFoundException("No se puede eliminar una comparativa de otro usuario");

        }
        else
            throw new NoSuchElementFoundException("La comparativa no existe en el sistema");

    }
}
