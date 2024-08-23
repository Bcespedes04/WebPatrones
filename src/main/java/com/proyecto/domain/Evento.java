package com.proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;
import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private int idEvento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(name = "nombre_evento", length = 100, nullable = false)
    private String nombreEvento;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "horario", nullable = false)
    private Time horario;

    @Column(name = "estado")
    private Boolean estado;
    
    public int getId() {
        return this.idEvento;
    }

    public Evento(int idEvento, User user, String nombreEvento, Date fecha, Time horario, Boolean estado) {
        this.idEvento = idEvento;
        this.user = user;
        this.nombreEvento = nombreEvento;
        this.fecha = fecha;
        this.horario = horario;
        this.estado = estado;
    }

    public Evento() {
    }
    
    
}
