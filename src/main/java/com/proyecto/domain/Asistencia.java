package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "asistencia")
public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long idAsistencia;

    @Column(name = "clase")
    private String clase;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "horario")
    private Time horario;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @Column(name = "estado")
    private Boolean estado;

    // Getters y Setters adicionales si es necesario
    public Long getId() {
        return this.idAsistencia;
    }

    public void setIdAsistencia(Long idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}


