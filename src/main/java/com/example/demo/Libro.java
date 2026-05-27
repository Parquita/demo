package com.example.demo;

public class Libro extends Recurso {
    private int numeroPaginas;

    public Libro(int id, String titulo, String autor, String editorial, String genero, int copias, int numeroPaginas) {
        super(id, titulo, autor, editorial, genero, copias);
        this.numeroPaginas = numeroPaginas;
    }

    @Override
    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }
}