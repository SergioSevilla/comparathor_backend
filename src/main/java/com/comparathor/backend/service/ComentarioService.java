package com.comparathor.backend.service;

import com.comparathor.backend.entity.Comentario;
import com.comparathor.backend.entity.Producto;
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
public class ComentarioService {

    @Autowired
    private final ComentarioRepository repository;

    @Autowired
    private final UserInfoRepository repositoryUser;

    @Autowired
    private final ProductoRepository repositoryProducto;

    public ComentarioService(final ComentarioRepository repository, final UserInfoRepository repositoryUser,
                           ProductoRepository repositoryProducto) {
        this.repository = repository;
        this.repositoryUser = repositoryUser;
        this.repositoryProducto = repositoryProducto;

    }


    public List<Comentario> getAllCommentsItem(int id, UserInfoDetails user) {
        Optional<Producto> productoOpt = repositoryProducto.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            if (producto.getObjectEstado().getId()==2)
            {
                return repository.findByProductoAndDeletedAtNull(productoOpt.get());
            }
            else
            {
                Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
                if (usuarioOpt.isPresent())
                {
                    if (producto.getObjectUsuario().getId() == usuarioOpt.get().getId())
                    {
                        return repository.findByProductoAndDeletedAtNull(productoOpt.get());
                    }
                }
                else
                    throw new NoSuchElementFoundException("El usuario aún no existe en el sistema");
            }
                throw new NoSuchElementFoundException("El producto aún no está disponible");
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");


    }

    public List<Comentario> getAllCommentsItemAdmin(int id) {
        Optional<Producto> productoOpt = repositoryProducto.findById(id);
        if (productoOpt.isPresent())
        {
            return repository.findByProducto(productoOpt.get());
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");

    }

    public Comentario addComentario(int id, UserInfoDetails user, Comentario comentario) {
        Optional<Producto> productoOpt = this.repositoryProducto.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
            if ((producto.getEstado()==2) || ((producto.getEstado()==1) && (producto.getObjectUsuario().getId() == usuarioOpt.get().getId())))
            {
                comentario.setProducto(producto);
                comentario.setUsuario(usuarioOpt.get());
                comentario.setCreated_at(new Date(System.currentTimeMillis()));
                comentario.setUpdated_at(new Date(System.currentTimeMillis()));
                this.repository.save(comentario);
                return comentario;
            }
            else {
               throw new NoSuchElementFoundException("No se puede añadir un comentario a un producto provisional");
            }


        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");



    }

    public Comentario modifyComentario(int id, UserInfoDetails user, Comentario comentario) {
        Optional<Comentario> comentarioOpt = this.repository.findByIdAndDeletedAtNull(id);
        if (comentarioOpt.isPresent())
        {
            Comentario comentarioToModify = comentarioOpt.get();
            Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());

            if ((comentarioToModify.getUsuarioObj().getId() == usuarioOpt.get().getId()) ||
                    (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
            {
                comentarioToModify.setTexto(comentario.getTexto());
                comentarioToModify.setUpdated_at(new Date(System.currentTimeMillis()));
                this.repository.save(comentarioToModify);
                return comentarioToModify;
            }
            else {
                throw new NoSuchElementFoundException("No se puede modificar un comentario de otro usuario");
            }
        }
        else
            throw new NoSuchElementFoundException("El comentario no existe en el sistema");

    }

    public Comentario deleteComentario(int id, UserInfoDetails user) {
        Optional<Comentario> comentarioOpt = this.repository.findByIdAndDeletedAtNull(id);
        if (comentarioOpt.isPresent())
        {
            Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
            Comentario comentarioToDelete = comentarioOpt.get();
            if ((comentarioToDelete.getUsuarioObj().getId() == usuarioOpt.get().getId()) ||
                    (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
            {
                repository.delete(comentarioToDelete);
                return null;
            }
            else {
                throw new NoSuchElementFoundException("No se puede eliminar un comentario de otro usuario");
            }
        }
        else
            throw new NoSuchElementFoundException("El comentario no existe en el sistema");
    }
}
