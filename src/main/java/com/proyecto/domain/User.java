package com.proyecto.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    
    @Column(name = "genero")
    private String genero;

    @Column(name = "email")
    private String email;

    @Column(name = "contrasena")
    private String password;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "ciudad")
    private String ciudad;
    
    @Column(name = "zip_codigo")
    private String zipCodigo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "fecha_solicitud_recuperacion")
    private Date fechaSolicitudRecuperacion;

    @Column(name = "ruta_imagen")
    private String rutaImagen;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "peso")
    private int peso;

    @Column(name = "pais")
    private String pais;

    
    public boolean isActive() {
        return this.activo;
    }

   
    public Long getId() {
        return this.idUsuario;
    }
}





