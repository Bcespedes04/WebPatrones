package com.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.sql.Time;

@Entity
@Table(name = "reserva")
public class Zumba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_reserva;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "clase")
    private String clase;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "horario")
    private Time horario;

    @Column(name = "estado")
    private boolean estado;

    // Getters y Setters
    public int getIdReserva() {
        return id_reserva;
    }

    public void setIdReserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}