package com.comparathor.backend.service;

import com.comparathor.backend.entity.*;
import com.comparathor.backend.exception.ForbiddenException;
import com.comparathor.backend.exception.NoSuchElementFoundException;
import com.comparathor.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Value("${picture.path}")
    private String uploadDir;

    @Autowired
    private final ProductoRepository repository;

    @Autowired
    private final UserInfoRepository repositoryUser;

    @Autowired
    private final EstadoRepository repositoryEstado;

    @Autowired
    private final CategoriaRepository repositoryCategoria;

    @Autowired
    private final AtributoValorRepository repositoryAtributoValor;

    @Autowired
    private final ComentarioRepository repositoryComentario;

    @Autowired
    private final PrecioRepository repositoryPrecio;

    public ProductoService(final ProductoRepository repository, final UserInfoRepository repositoryUser,
                           EstadoRepository repositoryEstado, CategoriaRepository repositoryCategoria, AtributoValorRepository repositoryAtributoValor, ComentarioRepository repositoryComentario, PrecioRepository repositoryPrecio) {
        this.repository = repository;
        this.repositoryUser = repositoryUser;
        this.repositoryEstado = repositoryEstado;
        this.repositoryCategoria = repositoryCategoria;

        this.repositoryAtributoValor = repositoryAtributoValor;
        this.repositoryComentario = repositoryComentario;
        this.repositoryPrecio = repositoryPrecio;
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
                        return clearDelete(repository.findByUsuarioAndEstado(usuario, repositoryEstado.findById(estadoId).get()));
                    else
                        throw new NoSuchElementFoundException("El estado no existe en el sistema");

                }
                else return clearDelete(repository.findByUsuarioOrEstado(usuario, repositoryEstado.findById(2).get()));
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }

    private List<Producto> clearDelete(List<Producto> resultado) {
        resultado.removeIf((producto) -> (producto.getDeleted_at() != null));
        return resultado;
    }

    public Producto addProducto(UserInfoDetails user, Producto producto, int categoriaId) {
        Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
        if (usuarioOpt.isPresent())
        {
            Optional<Categoria> categoriaOpt = repositoryCategoria.findByIdAndDeletedAtNull(categoriaId);
            if (categoriaOpt.isPresent())
            {
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
                throw new NoSuchElementFoundException("La categoría no existe en el sistema");
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }

    public Producto modifyProducto(int id, Producto producto, UserInfoDetails user)
    {
        Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
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
                Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
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

    public Producto saveImage(MultipartFile multipartFile, int id, UserInfoDetails user)
    {
        Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            if ((producto.getObjectUsuario().getId()==repositoryUser.findByEmail(user.getUsername()).get().getId()) &&
                    (producto.getEstado()==1))
            {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String fileName = producto.getObjectUsuario().getId()+"_"+id+"_"+timestamp.getTime();
                saveFile(multipartFile,fileName);
                producto.setFoto(fileName);
                this.repository.save(producto);
                return producto;

            }
            else
                throw new NoSuchElementFoundException("No se puede guardar la imagen");
        }
        else
            throw new NoSuchElementFoundException("El prodcuto no existe en el sistema");
    }

    private void saveFile(MultipartFile multipartFile, String fileName)  {
        Path uploadPath = Paths.get(this.uploadDir);
        try {
            if (!Files.exists(uploadPath)) {

                    Files.createDirectories(uploadPath);

            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getImage(int id, UserInfoDetails user) {
        String fileName = "";
        Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            if ((producto.getObjectUsuario().getId()==repositoryUser.findByEmail(user.getUsername()).get().getId()) ||
                    (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
            {
                if (producto.getFoto()!=null)
                {
                    fileName = productoOpt.get().getFoto();
                    File fi = new File(this.uploadDir+fileName);
                    byte[] image;
                    try {
                        image = Files.readAllBytes(fi.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.valueOf("image/jpeg"))
                            .body(image);
                }
                else
                    throw new NoSuchElementFoundException("El producto no tiene foto");
            }
            else
                throw new NoSuchElementFoundException("Sin permisos");

        }
        else
            throw new NoSuchElementFoundException("El producto o la foto no existe en el sistema");





    }

    public Producto deletePicture(int id, UserInfoDetails user)
    {
        Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            Producto producto = productoOpt.get();
            if (producto.getFoto() == null)
            {
                return producto;
            }
            else {
                if ((producto.getObjectUsuario().getId()==repositoryUser.findByEmail(user.getUsername()).get().getId()) ||
                        (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))))
                {
                    deleleteImage(producto.getFoto());
                    producto.setFoto(null);
                    repository.save(producto);
                    return producto;
                }
                else {
                    throw new NoSuchElementFoundException("Sin permisos");
                }
            }
        }
        else
            throw new NoSuchElementFoundException("El prodcuto no existe en el sistema");
    }

    private void deleleteImage(String foto) {

        File fi = new File(this.uploadDir+foto);
        fi.delete();
    }

    public Producto deleteProducto(int id, UserInfoDetails user) {
        Optional<Producto> productoOpt = repository.findByIdAndDeletedAtNull(id);
        if (productoOpt.isPresent())
        {
            if (((productoOpt.get().getObjectUsuario().getId()==repositoryUser.findByEmail(user.getUsername()).get().getId()) &&
                    productoOpt.get().getObjectEstado().getId()==1) ||
                    (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))) {
                repository.delete(productoOpt.get());
                //borrar atributosValor
                List<AtributoValor> atributosValor = repositoryAtributoValor.findByProducto(productoOpt.get());
                for (AtributoValor atributo : atributosValor) repositoryAtributoValor.delete(atributo);
                //borrar comentarios
                List<Comentario> comentarios = repositoryComentario.findByProducto(productoOpt.get());
                for (Comentario comentario : comentarios) repositoryComentario.delete(comentario);
                //borrar precios
                List<Precio> precios = repositoryPrecio.findByProducto(productoOpt.get());
                for (Precio precio : precios) repositoryPrecio.delete(precio);
                return null;
            }
            else
                throw new ForbiddenException("No tiene permisos para borrar este producto");
        }
        else
            throw new NoSuchElementFoundException("El producto no se encuentra en el sistema");
    }

    public List<Producto>  getProductoByCat(UserInfoDetails user, int categoriaId) {

        Optional<Usuario> usuarioOpt = repositoryUser.findByEmail(user.getUsername());
        if (usuarioOpt.isPresent())
        {
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
            {
                return repository.findByCategoria(repositoryCategoria.findById(categoriaId));
            }
            else {
                Usuario usuario = usuarioOpt.get();
                return clearDelete(repository.findByUsuarioAndCategoriaOrEstadoAndCategoria(usuario, repositoryCategoria.findById(categoriaId), repositoryEstado.findById(2).get(),repositoryCategoria.findById(categoriaId)));
            }

        }
        else
        {
            throw new NoSuchElementFoundException("El usuario no existe en el sistema");
        }
    }
}
