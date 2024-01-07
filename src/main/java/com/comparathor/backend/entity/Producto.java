package com.comparathor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;
import java.util.List;

@Entity(name = "Producto")
@Table(name = "Producto", schema = "comparathor")
@SQLDelete(sql = "UPDATE Producto SET deleted_at = NOW() WHERE id= ?")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at" )
    private java.util.Date deletedAt;

    private String nombre;

    private String foto;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "estadoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Estado estado;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usuarioId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Usuario usuario;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CategoriaId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")
    private List<AtributoValor> atributos;

    public List<AtributoValor> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<AtributoValor> atributos) {
        this.atributos = atributos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getEstado() {
        return estado.getId();
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario.getEmail();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getCategoria() {
        return categoria.getId();
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @JsonIgnore
    public Usuario getObjectUsuario()
    {
        return this.usuario;
    }

    @JsonIgnore
    public Estado getObjectEstado()
    {
        return this.estado;
    }

    @JsonIgnore
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
