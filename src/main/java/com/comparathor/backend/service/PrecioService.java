package com.comparathor.backend.service;

import com.comparathor.backend.entity.Origen;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.entity.Precio;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository repository;

    @Autowired
    private OrigenRepository repositoryOrigen;

    @Autowired
    private ProductoRepository repositoryProducto;

    @Autowired
    private UserInfoRepository repositoryUsuario;

    @Autowired
    private EstadoRepository repositoryEstado;

    public PrecioService(PrecioRepository repository, OrigenRepository repositoryOrigen,
                         ProductoRepository repositoryProducto, UserInfoRepository repositoryUsuario,
                         EstadoRepository repositoryEstado) {
        this.repository = repository;
        this.repositoryOrigen = repositoryOrigen;
        this.repositoryProducto = repositoryProducto;
        this.repositoryUsuario = repositoryUsuario;
        this.repositoryEstado = repositoryEstado;
    }

    public List<Precio> getPreciosProducto(UserInfoDetails user, int id) {
        //ver si el producto existe
        Optional<Producto> productoOptional = repositoryProducto.findById(id);
        if (productoOptional.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                return getPreciosAdmin(productoOptional.get());
            }
            else {
                return getPreciosUser(productoOptional.get(),user);
            }
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    private List<Precio> getPreciosUser(Producto producto, UserInfoDetails user) {
        Usuario usuario = repositoryUsuario.findByEmail(user.getUsername()).get();
        if ((producto.getEstado()==2) || ((producto.getEstado()==1) && (producto.getUsuario().equals(user.getUsername()))))
        {
            return repository.findByProducto(producto);
        }
        else
            throw new NoSuchElementFoundException("El producto aún no está disponible");
    }

    private List<Precio> getPreciosAdmin(Producto producto) {
        return repository.findByProducto(producto);
    }

    public Precio addPreciosProducto(UserInfoDetails user, Precio precio) {
        Optional<Producto> productoOptional = repositoryProducto.findById(precio.getProducto());
        if (productoOptional.isPresent())
        {
            Optional<Origen> origenOptional = repositoryOrigen.findById((int) precio.getOrigen());
            if (origenOptional.isPresent())
            {
                if (repository.findByProductoAndOrigen(productoOptional.get(),origenOptional.get()).isPresent())
                {
                    throw new NoSuchElementFoundException("Ya existe un precio en el sistema");
                }
                else
                {
                    if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
                    {
                        return addPrecioAdmin(precio, productoOptional.get(), origenOptional.get());
                    }
                    else {
                        return addPrecioUser(precio, productoOptional.get(), origenOptional.get(), user);
                    }
                }

            }
            else
                throw new NoSuchElementFoundException("El origen no existe en el sistema");
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    private Precio addPrecioUser(Precio precio, Producto producto, Origen origen, UserInfoDetails user) {

            if ((producto.getEstado()==2) || ((producto.getEstado()==1) && (producto.getUsuario().equals(user.getUsername()))))
            {
                Precio precioToAdd = new Precio();
                precioToAdd.setOrigen(origen);
                precioToAdd.setProducto(producto);
                precioToAdd.setValor(precio.getValor());
                precioToAdd.setUrl(precio.getUrl());
                precioToAdd.setCreated_at(new Date(System.currentTimeMillis()));
                precioToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
                precioToAdd.setEstado(repositoryEstado.findById(1).get());
                repository.save(precioToAdd);
                return precioToAdd;
            }
            else
                throw new NoSuchElementFoundException("El producto aún no está disponible");

    }

    private Precio addPrecioAdmin(Precio precio, Producto producto, Origen origen) {

            Precio precioToAdd = new Precio();
            precioToAdd.setOrigen(origen);
            precioToAdd.setProducto(producto);
            precioToAdd.setValor(precio.getValor());
            precioToAdd.setUrl(precio.getUrl());
            precioToAdd.setCreated_at(new Date(System.currentTimeMillis()));
            precioToAdd.setUpdated_at(new Date(System.currentTimeMillis()));
            precioToAdd.setEstado(repositoryEstado.findById(2).get());
            repository.save(precioToAdd);
            return precioToAdd;

    }

    public Precio modifyPrecio(UserInfoDetails user, int id, Precio precio) {
        Optional<Precio> precioOptional = repository.findById(id);
        if (precioOptional.isPresent()) {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                return modifyPrecioAdmin(id, precio);
            }
            else {
                return modifyPrecioUser(id, precio, user);
            }
        }
        else
                throw new NoSuchElementFoundException("El precio no existe en el sistema");
    }

    private Precio modifyPrecioUser(int id, Precio precio, UserInfoDetails user) {

        Optional<Origen> origenOptional = repositoryOrigen.findById((int) precio.getOrigen());
        if (origenOptional.isPresent())
        {

            Precio precioToModify = repository.findById(id).get();
            if (repositoryProducto.findById(precioToModify.getProducto()).get().getObjectUsuario().getEmail().equals(user.getUsername()))
            {
                precioToModify.setEstado(repositoryEstado.findById(1).get());
                precioToModify.setValor(precio.getValor());
                precioToModify.setProducto(repositoryProducto.findById(precio.getProducto()).get());
                precioToModify.setUrl(precio.getUrl());
                repository.save(precioToModify);
                return precioToModify;
            }
            else
                throw new NoSuchElementFoundException("No se peude modificar el precio de un producto de otro usuario");
        }
        else
            throw new NoSuchElementFoundException("El origen no existe en el sistema");
    }

    private Precio modifyPrecioAdmin(int id, Precio precio) {
        Optional<Origen> origenOptional = repositoryOrigen.findById((int) precio.getOrigen());
        if (origenOptional.isPresent())
        {
            Precio precioToModify = repository.findById(id).get();
            precioToModify.setEstado(repositoryEstado.findById(Math.toIntExact(precio.getEstado())).get());
            precioToModify.setValor(precio.getValor());
            precioToModify.setProducto(repositoryProducto.findById(precio.getProducto()).get());
            precioToModify.setUrl(precio.getUrl());
            repository.save(precioToModify);
            return precioToModify;
        }
        else
            throw new NoSuchElementFoundException("El origen no existe en el sistema");
    }
}
