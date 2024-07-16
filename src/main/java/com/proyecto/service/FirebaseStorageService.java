package com.proyecto.service;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseStorageService {

    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id);

    //El BuketName es el <id_del_proyecto> + ".appspot.com"
    final String BucketName = "gimasio-proyecto.appspot.com"; //Reemplazado x dato que estaba en firebase

    //Esta es la ruta básica
    final String rutaSuperiorStorage = "proyecto";

    //Ubicación donde se encuentra el archivo de configuración Json
    final String rutaJsonFile = "firebase";

    //El nombre del archivo Json
    final String archivoJsonFile = "gimasio-proyecto-firebase.json"; //Json que esta en firebase
}
