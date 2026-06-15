package com.example.apiyazumy.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "codigo_producto", length = 50)
    private String codigoProducto;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String presentacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer stock;

    @Column(length = 255)
    private String imagen;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Producto() {
    }

    public Producto(Integer idProducto, String codigoProducto, String nombre, String descripcion, 
                    String presentacion, BigDecimal precio, Integer stock, String imagen) {
        this.idProducto = idProducto;
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.presentacion = presentacion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.activo = true;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
