package com.comparathor.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "ComparativaProducto")
@Table(name = "ComparativaProducto", schema = "comparathor")
public class ComparativaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updated_at;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date deleted_at;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "comparativaId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Comparativa comparativa;

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "productoId", referencedColumnName = "id", nullable = false, insertable = true, updatable = true)
    private Producto producto;

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
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Comparativa getComparativa() {
        return comparativa;
    }

    public void setComparativa(Comparativa comparativa) {
        this.comparativa = comparativa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
