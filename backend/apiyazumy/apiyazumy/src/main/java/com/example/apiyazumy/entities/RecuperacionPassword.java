package com.example.apiyazumy.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacion_password")
public class RecuperacionPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recuperacion")
    private Integer idRecuperacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean usado;

    public RecuperacionPassword() {
    }

    public RecuperacionPassword(Usuario usuario, String token, LocalDateTime fechaExpiracion) {
        this.usuario = usuario;
        this.token = token;
        this.fechaExpiracion = fechaExpiracion;
        this.usado = false;
    }

    public Integer getIdRecuperacion() {
        return idRecuperacion;
    }

    public void setIdRecuperacion(Integer idRecuperacion) {
        this.idRecuperacion = idRecuperacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }
}
