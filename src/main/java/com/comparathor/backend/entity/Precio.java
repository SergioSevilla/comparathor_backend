package com.comparathor.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Entity(name = "Precio")
@Table(name = "Precio", schema = "comparathor")
@SQLDelete(sql = "UPDATE Precio SET deleted_at = NOW() WHERE id= ?")
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double valor;

    private String url;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origenId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Origen origen;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "productoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Producto producto;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "estadoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Estado estado;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at" )
    private java.util.Date deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonIgnore
    public Origen getOrigenObject() {
        return origen;
    }

    public long getOrigen() {
        return origen.getId();
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
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
    public Producto getProductoObject() {
        return producto;
    }

    public Long getProducto() {
        return producto.getId();
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @JsonIgnore
    public Estado getEstadoObject() {
        return estado;
    }

    public Long getEstado() {
        return estado.getId();
    }


    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
