package com.example.demo;


public class Usuario {
    private int id;
    private String usuario;
    private String correo;
    private String contraseña;

    public Usuario(int id, String usuario, String correo, String contraseña) {
        this.id = id;
        this.usuario = usuario;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getContrasena() {
        return contraseña;
    }

    public String mostrarInformacion() {
        return "ID: " + id +
                "\nUsuario: " + usuario +
                "\nCorreo: " + correo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}