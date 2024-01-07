package com.comparathor.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Entity(name = "Comentario")
@Table(name = "Comentario", schema = "comparathor")
@SQLDelete(sql = "UPDATE Comentario SET deleted_at = NOW() WHERE id= ?")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at" )
    private java.util.Date deletedAt;


    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuarioId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Usuario usuario;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "productoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deletedAt;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deletedAt = deleted_at;
    }

    @JsonIgnore
    public Usuario getUsuarioObj() {
        return usuario;
    }

    public int getUsuario() {
        return usuario.getId();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @JsonIgnore
    public Producto getProductoObj() {
        return producto;
    }

    public long getProducto() {
        return producto.getId();
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
