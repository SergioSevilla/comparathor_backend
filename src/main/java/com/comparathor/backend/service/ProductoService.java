package com.comparathor.backend.service;

import com.comparathor.backend.entity.Categoria;
import com.comparathor.backend.entity.Estado;
import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.CategoriaRepository;
import com.comparathor.backend.repository.EstadoRepository;
import com.comparathor.backend.repository.ProductoRepository;
import com.comparathor.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private final ProductoRepository repository;

    @Autowired
    private final UserInfoRepository repositoryUser;

    @Autowired
    private final EstadoRepository repositoryEstado;

    @Autowired
    private final CategoriaRepository repositoryCategoria;

    public ProductoService(final ProductoRepository repository, final UserInfoRepository repositoryUser,
                           EstadoRepository repositoryEstado, CategoriaRepository repositoryCategoria) {
        this.repository = repository;
        this.repositoryUser = repositoryUser;
        this.repositoryEstado = repositoryEstado;
        this.repositoryCategoria = repositoryCategoria;

    }
    public List<Producto> getAllProductos(UserInfoDetails user, Long estadoId) {
        Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
        if (usuarioOpt.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                if (estadoId != null)
                {
                    if (repositoryEstado.findById(estadoId).isPresent())
                        return repository.findByEstado(repositoryEstado.findById(estadoId).get());
                    else
                        throw new NoSuchElementFoundException("El estado no existe en el sistema");
                }
                else return repository.findAll();
            }
            else {
                Usuario usuario = usuarioOpt.get();
                if (estadoId != null)
                {
                    if (repositoryEstado.findById(estadoId).isPresent())
                        return repository.findByUsuarioAndEstado(usuario, repositoryEstado.findById(estadoId).get());
                    else
                        throw new NoSuchElementFoundException("El estado no existe en el sistema");

                }
                else return repository.findByUsuarioOrEstado(usuario, repositoryEstado.findById(2).get());
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }

    public Producto addProducto(UserInfoDetails user, Producto producto, int categoriaId) {
        Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
        if (usuarioOpt.isPresent())
        {
            Optional<Categoria> categoriaOpt = repositoryCategoria.findById(categoriaId);
            if (categoriaOpt.isPresent())
            {
                Estado estado = new Estado();

                producto.setCategoria(categoriaOpt.get());
                producto.setEstado(repositoryEstado.findById(1).get());
                producto.setUsuario(usuarioOpt.get());
                producto.setCreated_at(new Date(System.currentTimeMillis()));
                producto.setUpdated_at(new Date(System.currentTimeMillis()));
                repository.save(producto);
                return producto;
            }
            else
            {
                throw new NoSuchElementFoundException("El usuario no existe en el sistema");
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }

    public Producto modifyProducto(int id, Producto producto, UserInfoDetails user)
    {
        Optional<Producto> productoOpt = repository.findById(id);
        if (productoOpt.isPresent())
        {
            Producto productoMod= productoOpt.get();
            Usuario usuario = repositoryUser.findByEmail(user.getUsername()).get();
            if ((productoMod.getObjectUsuario().getId() == usuario.getId())
                    && (productoMod.getObjectEstado().getId() == 1))
            {

                Optional<Categoria> categoriaOpt = repositoryCategoria.findById(producto.getCategoria());
                if (categoriaOpt.isPresent()) {
                    productoMod.setNombre(producto.getNombre());
                    productoMod.setCategoria(categoriaOpt.get());
                    productoMod.setUpdated_at(new Date(System.currentTimeMillis()));
                    repository.save(productoMod);
                    return productoMod;
                }
                else
                    throw new NoSuchElementFoundException("No existe la categoría");
            }
            else
            {
                throw new NoSuchElementFoundException("No se puede modificar un producto en estado público o el dueño del producto no es usted");
            }


        }
        else throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    public Producto modifyProductoAdmin(int id, Producto producto)
    {
        Optional<Producto> productoOpt = repository.findById(id);
        if (productoOpt.isPresent())
        {
            Optional<Categoria> categoriaOpt = repositoryCategoria.findById(producto.getCategoria());
            Optional<Estado> EstadoOpt = repositoryEstado.findById(producto.getEstado());
            if (categoriaOpt.isPresent()) {
                if(EstadoOpt.isPresent())
                {
                    Producto productoMod = productoOpt.get();
                    productoMod.setNombre(producto.getNombre());
                    productoMod.setCategoria(categoriaOpt.get());
                    productoMod.setEstado(EstadoOpt.get());
                    productoMod.setUpdated_at(new Date(System.currentTimeMillis()));
                    repository.save(productoMod);
                    return productoMod;
                }
                else throw new NoSuchElementFoundException("No existe el estado");
            }
            else throw new NoSuchElementFoundException("No existe la categoría");
        }
        else throw new NoSuchElementFoundException("El producto no existe en el sistema");
    }

    public Producto getProducto(UserInfoDetails user, int id)
    {
        Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
        if (usuarioOpt.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                Optional<Producto> productoOpt = repository.findById(id);
                if (productoOpt.isPresent())
                {
                    return productoOpt.get();
                }
                else throw new NoSuchElementFoundException("El prodcuto no existe en el sistema");
            }
            else {
                Optional<Producto> productoOpt = repository.findById(id);
                if (productoOpt.isPresent())
                {
                    if((productoOpt.get().getEstado()==2) || (
                            (usuarioOpt.get().getId() == productoOpt.get().getObjectUsuario().getId())) &&
                            (productoOpt.get().getEstado()==1) )
                    {
                        return productoOpt.get();
                    }
                    else throw new NoSuchElementFoundException("El prodcuto aún no está disponible");
                }
                else throw new NoSuchElementFoundException("El prodcuto no existe en el sistema");
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }
}
