package com.proyecto.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "encuesta")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encuesta")
    private Long idEncuesta;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "limpieza")
    private int limpieza;

    @Column(name = "calidad")
    private int calidad;

    @Column(name = "mejoras")
    private String mejoras;

    @Column(name = "comentarios")
    private String comentarios;

    @ManyToOne
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private User usuario;
}
