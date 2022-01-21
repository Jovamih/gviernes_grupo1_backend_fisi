package com.example.appbuscatutor;

public class TutoresFavoritos {
    private String nombre;
    private String descripcion;
    private String foto;
    private int id;
    public int getId(){
        return id;
    }
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

    public String getFoto() {
        return foto;
    }
    public void setId(int id){
        this.id=id;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public TutoresFavoritos(String nombre, String descripcion, String foto, int id){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.foto=foto;
        this.id = id;
    }
}
