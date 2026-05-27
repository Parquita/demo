package com.example.demo;

import java.util.ArrayList;
import java.io.IOException;

public class Biblioteca {
    private UsuarioDAO usuarioDAO;
    private RecursoDAO recursoDAO;
    private PrestamoDAO prestamoDAO;

    public Biblioteca() {
        this.usuarioDAO = new UsuarioDAO();
        this.recursoDAO = new RecursoDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void registrarLibro(String titulo, String autor, String editorial, String genero,
                               int copias, int numeroPaginas) throws IOException {
        Libro libro = new Libro(0, titulo, autor, editorial, genero, copias, numeroPaginas);
        recursoDAO.insertar(libro);
    }

    public void registrarRevista(String titulo, String autor, String editorial, String genero,
                                 int copias, int edicion) throws IOException {
        Revista revista = new Revista(0, titulo, autor, editorial, genero, copias, edicion);
        recursoDAO.insertar(revista);
    }

    public Recurso buscarRecursoPorId(int id) throws IOException {
        return recursoDAO.obtenerPorId(id);
    }

    public Usuario buscarUsuarioPorId(int id) throws IOException {
        return usuarioDAO.obtenerPorId(id);
    }

    public void prestarRecurso(int idUsuario, int idRecurso, String fechaDevolucion)
            throws RecursoNoDisponibleException, IOException {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        Recurso recurso = buscarRecursoPorId(idRecurso);
        if (usuario == null) {
            throw new RecursoNoDisponibleException("El usuario no existe");
        }
        if (recurso == null) {
            throw new RecursoNoDisponibleException("El recurso no existe");
        }
        if (fechaDevolucion == null || fechaDevolucion.isEmpty()) {
            throw new RecursoNoDisponibleException("Debe ingresar una fecha de devolución");
        }
        if (!recurso.prestar()) {
            throw new RecursoNoDisponibleException("No hay copias disponibles de este recurso.");
        }
        Prestamo prestamo = new Prestamo(0, idUsuario, idRecurso, fechaDevolucion);
        prestamoDAO.insertar(prestamo);
        recursoDAO.actualizarDisponibles(idRecurso, recurso.getCopias());
    }

    public ArrayList<Recurso> getRecursos() throws IOException {
        return new ArrayList<>(recursoDAO.obtenerTodos());
    }

    public ArrayList<Usuario> getUsuarios() throws IOException {
        return new ArrayList<>(usuarioDAO.obtenerTodos());
    }

    public ArrayList<Prestamo> getPrestamos() throws IOException {
        return new ArrayList<>(prestamoDAO.obtenerTodos());
    }

    public Prestamo buscarPrestamoPorId(int id) throws IOException {
        return prestamoDAO.obtenerPorId(id);
    }

    public void devolverRecurso(int idPrestamo) throws RecursoNoDisponibleException, IOException {
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);
        if (prestamo == null) {
            throw new RecursoNoDisponibleException("El préstamo no existe");
        }
        if (prestamo.isDevuelto()) {
            throw new RecursoNoDisponibleException("Este préstamo ya ha sido devuelto");
        }
        Recurso recurso = recursoDAO.obtenerPorId(prestamo.getRecursoId());
        recurso.devolver();
        prestamoDAO.marcarDevuelto(idPrestamo);
        recursoDAO.actualizarDisponibles(prestamo.getRecursoId(), recurso.getCopias());
    }

    public int totalRecursos() throws IOException {
        return recursoDAO.obtenerTodos().size();
    }

    public int totalUsuarios() throws IOException {
        return usuarioDAO.obtenerTodos().size();
    }

    public int totalPrestamos() throws IOException {
        return prestamoDAO.obtenerTodos().size();
    }

    public void eliminarRecurso(int id) throws Exception {
        recursoDAO.eliminar(id);
    }

    public void actualizarRecurso(int id, java.util.Map<String, Object> datos) throws Exception {
        com.google.gson.JsonObject data = new com.google.gson.JsonObject();
        data.addProperty("titulo", datos.get("titulo").toString());
        data.addProperty("autor", datos.get("autor").toString());
        data.addProperty("editorial", datos.get("editorial").toString());
        data.addProperty("genero", datos.get("genero").toString());
        data.addProperty("copias", Integer.parseInt(datos.get("copias").toString()));
        
        SupabaseConfig.patch("recursos", id, data);
    }

    public String prestar(int idRecurso, int idUsuario, String fechaDevolucion, String contrasenaIngresada) {
        try {
            String userRes = SupabaseConfig.get("usuarios", "id=eq." + idUsuario);
            com.google.gson.JsonArray arrayUser = com.google.gson.JsonParser.parseString(userRes).getAsJsonArray();
            if (arrayUser.size() == 0) return "Usuario no encontrado en la base de datos.";
            
            String claveReal = arrayUser.get(0).getAsJsonObject().get("contraseña").getAsString();
            if (!claveReal.equals(contrasenaIngresada)) {
                return "Contraseña incorrecta. Préstamo denegado.";
            }

            String resData = SupabaseConfig.get("recursos", "id=eq." + idRecurso);
            com.google.gson.JsonArray arrayRecursos = com.google.gson.JsonParser.parseString(resData).getAsJsonArray();
            if (arrayRecursos.size() == 0) return "El recurso solicitado no existe.";
            
            int copiasTotales = arrayRecursos.get(0).getAsJsonObject().get("copias").getAsInt();

            String prestamosData = SupabaseConfig.get("prestamos", "id_recurso=eq." + idRecurso + "&devuelto=eq.false");
            com.google.gson.JsonArray arrayPrestamos = com.google.gson.JsonParser.parseString(prestamosData).getAsJsonArray();
            int copiasPrestadas = arrayPrestamos.size();

            if (copiasPrestadas >= copiasTotales) {
                return "No hay copias disponibles en este momento.";
            }

            com.google.gson.JsonObject nuevoPrestamo = new com.google.gson.JsonObject();
            nuevoPrestamo.addProperty("id_usuario", idUsuario);
            nuevoPrestamo.addProperty("id_recurso", idRecurso);
            nuevoPrestamo.addProperty("fecha_devolucion", fechaDevolucion);
            nuevoPrestamo.addProperty("devuelto", false);

            SupabaseConfig.post("prestamos", nuevoPrestamo);
            return "EXITO";

        } catch (Exception e) {
            System.err.println("Error al prestar: " + e.getMessage());
            return "Error interno en el servidor.";
        }
    }

    public String obtenerMisPrestamos(int idUsuario) {
        try {
            return SupabaseConfig.get("prestamos", "id_usuario=eq." + idUsuario + "&devuelto=eq.false");
        } catch (Exception e) {
            System.err.println("Error al obtener préstamos: " + e.getMessage());
            return "[]";
        }
    }

    public boolean registrarUsuario(String usuario, String correo, String contrasena) {
        try {
            String resData = SupabaseConfig.get("usuarios", "correo=eq." + correo);
            com.google.gson.JsonArray arrayUsuarios = com.google.gson.JsonParser.parseString(resData).getAsJsonArray();
            
            if (arrayUsuarios.size() > 0) {
                return false; 
            }

            com.google.gson.JsonObject nuevoUsuario = new com.google.gson.JsonObject();
            nuevoUsuario.addProperty("usuario", usuario);
            nuevoUsuario.addProperty("correo", correo);
            nuevoUsuario.addProperty("contraseña", contrasena);
            
            SupabaseConfig.post("usuarios", nuevoUsuario);
            return true;

        } catch (Exception e) {
            System.err.println("Error al registrar en Supabase: " + e.getMessage());
            return false;
        }
    }

    public String iniciarSesion(String correo, String contrasena) {
        try {
            String resData = SupabaseConfig.get("usuarios", "correo=eq." + correo + "&contraseña=eq." + contrasena);
            com.google.gson.JsonArray arrayUsuarios = com.google.gson.JsonParser.parseString(resData).getAsJsonArray();
            
            if (arrayUsuarios.size() > 0) {
                com.google.gson.JsonObject user = arrayUsuarios.get(0).getAsJsonObject();
                
                user.remove("contraseña"); 
                
                return user.toString();
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            return null;
        }
    }

    public String obtenerTodosLosPrestamos() {
        try {
            return SupabaseConfig.get("prestamos", "devuelto=eq.false");
        } catch (Exception e) {
            System.err.println("Error al obtener todos los préstamos: " + e.getMessage());
            return "[]";
        }
    }
}