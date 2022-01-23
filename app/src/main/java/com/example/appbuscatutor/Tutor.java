package com.example.appbuscatutor;

import java.io.Serializable;

public class Tutor implements Serializable {
    private String nombre;
    private String descripcion;
    //private String foto;
    private int imgResource;
    private int id;

    //Constructor
    public Tutor (String nombre, String descripcion, int foto, int id){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.imgResource=foto;
        this.id = id;
    }

    //Getters and Setters
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

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    /*public String getFoto() {
        return foto;
    }*/

   /* public void setFoto(String foto) {
        this.foto = foto;
    }*/

    public void setId(int id){
        this.id=id;
    }

}
