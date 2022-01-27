package com.example.appbuscatutor;

import java.io.Serializable;

public class Tutor implements Serializable {
    private String nombre;
    private String descripcion;
    //private String foto;
    private String imgResource;
    private int id;

    private String especialidades;

    //Constructor
    public Tutor (String nombre, String descripcion, String foto, String esp, int id){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.imgResource=foto;
        this.especialidades = esp;
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

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource (String imgResource) {
        this.imgResource = imgResource;
    }


    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public String getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }
}
