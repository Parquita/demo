package com.example.demo;


import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    public void insertar(Prestamo prestamo) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("id_usuario", prestamo.getUsuarioId());
        data.addProperty("id_recurso", prestamo.getRecursoId());
        data.addProperty("fecha_devolucion", prestamo.getFechaDevolucion());
        data.addProperty("devuelto", prestamo.isDevuelto());

        String response = SupabaseConfig.post("prestamos", data);
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            prestamo.setId(obj.get("id").getAsInt());
        }
    }

    public Prestamo obtenerPorId(int id) throws IOException {
        String query = "id=eq." + id;
        String response = SupabaseConfig.get("prestamos", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            return mapToPrestamo(obj);
        }
        return null;
    }

    public List<Prestamo> obtenerPorUsuario(int usuarioId) throws IOException {
        String query = "id_usuario=eq." + usuarioId;
        String response = SupabaseConfig.get("prestamos", query);
        
        List<Prestamo> prestamos = new ArrayList<>();
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        
        for (JsonElement element : array) {
            prestamos.add(mapToPrestamo(element.getAsJsonObject()));
        }
        
        return prestamos;
    }

    public List<Prestamo> obtenerActivos() throws IOException {
        String query = "devuelto=eq.false";
        String response = SupabaseConfig.get("prestamos", query);
        
        List<Prestamo> prestamos = new ArrayList<>();
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        
        for (JsonElement element : array) {
            prestamos.add(mapToPrestamo(element.getAsJsonObject()));
        }
        
        return prestamos;
    }

    public void marcarDevuelto(int id) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("devuelto", true);
        data.addProperty("fecha_devolucion_real", System.currentTimeMillis());
        SupabaseConfig.patch("prestamos", id, data);
    }

    public List<Prestamo> obtenerTodos() throws IOException {
        String response = SupabaseConfig.get("prestamos", "select=*");
        List<Prestamo> prestamos = new ArrayList<>();

        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        for (JsonElement element : array) {
            prestamos.add(mapToPrestamo(element.getAsJsonObject()));
        }
        
        return prestamos;
    }

    public void eliminar(int id) throws IOException {
        SupabaseConfig.delete("prestamos", id);
    }

    private Prestamo mapToPrestamo(JsonObject obj) {
        int id = obj.get("id").getAsInt();
        int usuarioId = obj.get("id_usuario").getAsInt();
        int recursoId = obj.get("id_recurso").getAsInt();
        String fechaDevolucion = obj.get("fecha_devolucion").getAsString();
        boolean devuelto = obj.get("devuelto").getAsBoolean();

        Prestamo prestamo = new Prestamo(id, usuarioId, recursoId, fechaDevolucion);
        prestamo.setDevuelto(devuelto);
        return prestamo;
    }
}
