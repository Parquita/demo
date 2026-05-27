package com.example.demo;

public class Recurso {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private int copias;
    private int copiasDisponibles;
    
    private String tipo; 
    
    public Recurso(int id, String titulo, String autor, String editorial, String genero, int copias) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
        this.copias = copias;
        this.copiasDisponibles = copias;
        
        this.tipo = this.getClass().getSimpleName().toLowerCase();
    }
    
    public boolean prestar() {
        if (copiasDisponibles > 0) {
            copiasDisponibles--;
            return true;
        }
        return false;
    }
    public void devolver() {
        if (copiasDisponibles < copias) {
            copiasDisponibles++;
        }
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getGenero() { return genero; }
    public int getCopias() { return copias; }
    public int getCopiasDisponibles() { return copiasDisponibles; }
    public void setCopiasDisponibles(int copiasDisponibles) { this.copiasDisponibles = copiasDisponibles; }
    
    public String getTipo() { return tipo; }

    public Integer getNumeroPaginas() { return null; }
    public Integer getEdicion() { return null; }
}