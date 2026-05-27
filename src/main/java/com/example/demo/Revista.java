package com.example.demo;

public class Revista extends Recurso {
    private int edicion;

    public Revista(int id, String titulo, String autor, String editorial, String genero, int copias, int edicion) {
        super(id, titulo, autor, editorial, genero, copias);
        this.edicion = edicion;
    }

    // Spring Boot necesita este método exacto para enviarlo como JSON
    @Override
    public Integer getEdicion() {
        return edicion;
    }
}