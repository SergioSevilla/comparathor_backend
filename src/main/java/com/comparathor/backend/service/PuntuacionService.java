package com.comparathor.backend.service;

import com.comparathor.backend.entity.Producto;
import com.comparathor.backend.entity.Puntuacion;
import com.comparathor.backend.entity.Usuario;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.ProductoRepository;
import com.comparathor.backend.repository.PuntuacionRepository;
import com.comparathor.backend.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PuntuacionService {


    @Autowired
    private final PuntuacionRepository repository;

    @Autowired
    private final UserInfoRepository repositoryUser;

    @Autowired
    private final ProductoRepository repositoryProducto;

    public PuntuacionService (final PuntuacionRepository repository, final UserInfoRepository repositoryUser,
                              ProductoRepository repositoryProducto)
    {
        this.repository = repository;
        this.repositoryUser = repositoryUser;
        this.repositoryProducto = repositoryProducto;
    }

    public List<Puntuacion> getScoresItem(int id) {
        Optional<Producto> productoOpt = repositoryProducto.findById(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            return repository.findByProducto(productoOpt.get());
        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");

    }

    public Puntuacion modifyPuntuacion(int id, Puntuacion puntuacion, UserInfoDetails user)
    {
        Optional<Puntuacion> puntuacionOpt = this.repository.findById(id);
        if (puntuacionOpt.isPresent())
        {
            Puntuacion puntuacionToModify = puntuacionOpt.get();
            Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
            if ((puntuacionToModify.getUsuarioObj().getId() == usuarioOpt.get().getId()) ||
                    (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
            {
                puntuacionToModify.setValor(puntuacion.getValor());
                puntuacionToModify.setUpdated_at(new Date(System.currentTimeMillis()));
                this.repository.save(puntuacionToModify);
                return puntuacionToModify;
            }
            else {
                throw new NoSuchElementFoundException("No se puede modificar una puntuación de otro usuario");
            }
        }
        else
            throw new NoSuchElementFoundException("La puntuación no existe en el sistema");
    }

    public Puntuacion addPuntuacion(int id, Puntuacion puntuacion, UserInfoDetails user) {
        Optional<Producto> productoOpt = repositoryProducto.findById(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
            Optional<Puntuacion> puntuacionOpt = repository.findByProductoAndUsuario(producto,usuarioOpt.get());
            if (puntuacionOpt.isPresent())
            {
                throw new NoSuchElementFoundException("La puntuacion ya existe en el sistema");
            }
            {
                Puntuacion puntuacionToModify = new Puntuacion();
                puntuacionToModify.setValor(puntuacion.getValor());
                puntuacionToModify.setProducto(producto);
                puntuacionToModify.setUsuario(usuarioOpt.get());
                puntuacionToModify.setUpdated_at(new Date(System.currentTimeMillis()));
                puntuacionToModify.setCreated_at(new Date(System.currentTimeMillis()));
                this.repository.save(puntuacionToModify);
                return puntuacionToModify;
            }

        }
        else
            throw new NoSuchElementFoundException("El producto no existe en el sistema");

    }
}
