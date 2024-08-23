package com.proyecto.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "progreso")
public class Progreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_progreso")
    private Long idProgreso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Column(name = "peso")
    private Integer peso;

    @Column(name = "imc")
    private Integer imc;

    @Column(name = "altura")
    private Integer altura;

    @Column(name = "objetivo_cumplido")
    private Boolean objetivoCumplido;
}

