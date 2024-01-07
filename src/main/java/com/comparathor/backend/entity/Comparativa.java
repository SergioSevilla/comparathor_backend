package com.comparathor.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity(name = "Comparativa")
@Table(name = "Comparativa", schema = "comparathor")
public class Comparativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuarioId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "comparativa")
    private List<ComparativaProducto> comparativas;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date deleted_at;

    String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Usuario getUsuarioObject() {
        return usuario;
    }

    public int getUsuario() {
        return usuario.getId();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
