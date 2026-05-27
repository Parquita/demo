package com.example.demo;


public class Prestamo {
    private int id;
    private Usuario usuario;
    private Recurso recurso;
    private int usuarioId;
    private int recursoId;
    private String fechaDevolucion;
    private boolean devuelto;

    // Constructor original (para compatibilidad)
    public Prestamo(int id, Usuario usuario, Recurso recurso, String fechaDevolucion) {
        this.id = id;
        this.usuario = usuario;
        this.recurso = recurso;
        this.usuarioId = usuario.getId();
        this.recursoId = recurso.getId();
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = false;
    }

    // Constructor nuevo (para usar con IDs de BD)
    public Prestamo(int id, int usuarioId, int recursoId, String fechaDevolucion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.recursoId = recursoId;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public void marcarComoDevuelto() {
        this.devuelto = true;
    }

    public String mostrarInformacion() {
        String usuarioStr = usuario != null ? usuario.getUsuario() : "ID: " + usuarioId;
        String recursoStr = recurso != null ? recurso.getTitulo() : "ID: " + recursoId;
        return "ID Prestamo: " + id + "\nUsuario: " + usuarioStr + "\nRecurso: " + recursoStr + "\nFecha devolución: " + fechaDevolucion + "\nDevuelto: " + (devuelto ? "Sí" : "No");
    }
}