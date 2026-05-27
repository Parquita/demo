package com.example.demo;

public class Recurso {
    private int id;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private int copias;
    private int copiasDisponibles;
    
    // NUEVO ATRIBUTO: Esto le dirá a la web si es libro o revista
    private String tipo; 
    
    public Recurso(int id, String titulo, String autor, String editorial, String genero, int copias) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
        this.copias = copias;
        this.copiasDisponibles = copias;
        
        // Asignamos el tipo automáticamente basándonos en el nombre de la clase
        this.tipo = this.getClass().getSimpleName().toLowerCase();
    }
    
    // ... (Tus métodos prestar y devolver se quedan igual)
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
    
    // ... (Tus getters existentes se quedan igual)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getGenero() { return genero; }
    public int getCopias() { return copias; }
    public int getCopiasDisponibles() { return copiasDisponibles; }
    public void setCopiasDisponibles(int copiasDisponibles) { this.copiasDisponibles = copiasDisponibles; }
    
    // NUEVO GETTER PARA EL TIPO
    public String getTipo() { return tipo; }

    // NUEVOS MÉTODOS "DUMMY" PARA QUE SPRING BOOT LOS VEA (Las subclases los sobrescribirán)
    public Integer getNumeroPaginas() { return null; }
    public Integer getEdicion() { return null; }
}