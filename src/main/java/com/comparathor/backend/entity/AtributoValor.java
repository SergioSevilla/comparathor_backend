package com.comparathor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "AtributoValor")
@Table(name = "AtributoValor", schema = "comparathor")
public class AtributoValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String valor;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date deleted_at;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "atributoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Atributo atributo;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "productoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

    @JsonIgnore
    public Atributo getAtributoObject() {
        return atributo;
    }

    public Long getAtributo() {
        return atributo.getId();
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public Long getProducto() {
        return producto.getId();
    }

    @JsonIgnore
    public Producto getProductoObject() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
