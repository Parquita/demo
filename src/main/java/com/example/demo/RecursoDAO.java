package com.example.demo;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecursoDAO {
    public void insertar(Recurso recurso) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("tipo", recurso.getClass().getSimpleName().toUpperCase());
        data.addProperty("titulo", recurso.getTitulo());
        data.addProperty("autor", recurso.getAutor());
        data.addProperty("editorial", recurso.getEditorial());
        data.addProperty("genero", recurso.getGenero());
        data.addProperty("copias", recurso.getCopias());
        data.addProperty("copias_disponibles", recurso.getCopias());

        String response = SupabaseConfig.post("recursos", data);
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            recurso.setId(obj.get("id").getAsInt());
        }
    }

    public Recurso obtenerPorId(int id) throws IOException {
        String query = "id=eq." + id;
        String response = SupabaseConfig.get("recursos", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            String tipo = obj.get("tipo").getAsString();
            
            if ("LIBRO".equals(tipo)) {
                return obtenerLibroPorId(id);
            } else if ("REVISTA".equals(tipo)) {
                return obtenerRevistaPorId(id);
            }
        }
        return null;
    }

    public Libro obtenerLibroPorId(int id) throws IOException {
        String query = "id=eq." + id;
        String response = SupabaseConfig.get("libros", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            return new Libro(
                    obj.get("id").getAsInt(),
                    obj.get("Titulo").getAsString(),
                    obj.get("autor").getAsString(),
                    obj.get("editorial").getAsString(),
                    obj.get("genero").getAsString(),
                    obj.get("copias").getAsInt(),
                    obj.get("numero_paginas").getAsInt()
            );
        }
        return null;
    }

    public Revista obtenerRevistaPorId(int id) throws IOException {
        String query = "id=eq." + id;
        String response = SupabaseConfig.get("revistas", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            return new Revista(
                    obj.get("id").getAsInt(),
                    obj.get("titulo").getAsString(),
                    obj.get("autor").getAsString(),
                    obj.get("editorial").getAsString(),
                    obj.get("genero").getAsString(),
                    obj.get("copias").getAsInt(),
                    obj.get("edicion").getAsInt()
            );
        }
        return null;
    }

public List<Recurso> obtenerTodos() throws IOException {
        // 1. Traemos toda la info general de la tabla recursos
        String response = SupabaseConfig.get("recursos", "select=*");
        List<Recurso> recursos = new ArrayList<>();

        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            
            // Leemos los datos generales que SÍ están en la tabla recursos
            int id = obj.get("id").getAsInt();
            String tipo = obj.get("tipo").getAsString();
            String titulo = obj.has("titulo") && !obj.get("titulo").isJsonNull() ? obj.get("titulo").getAsString() : "Sin título";
            String autor = obj.has("autor") && !obj.get("autor").isJsonNull() ? obj.get("autor").getAsString() : "Desconocido";
            String editorial = obj.has("editorial") && !obj.get("editorial").isJsonNull() ? obj.get("editorial").getAsString() : "Desconocida";
            String genero = obj.has("genero") && !obj.get("genero").isJsonNull() ? obj.get("genero").getAsString() : "General";
            int copias = obj.has("copias") && !obj.get("copias").isJsonNull() ? obj.get("copias").getAsInt() : 0;

            if ("LIBRO".equals(tipo)) {
                // 2. Si es libro, vamos a la tabla 'libros' SOLO a buscar el numero_paginas
                int paginas = 0;
                String libRes = SupabaseConfig.get("libros", "id=eq." + id);
                JsonArray libArr = JsonParser.parseString(libRes).getAsJsonArray();
                if (libArr.size() > 0) {
                    JsonObject libObj = libArr.get(0).getAsJsonObject();
                    if (libObj.has("numero_paginas") && !libObj.get("numero_paginas").isJsonNull()) {
                        paginas = libObj.get("numero_paginas").getAsInt();
                    }
                }
                recursos.add(new Libro(id, titulo, autor, editorial, genero, copias, paginas));
                
            } else if ("REVISTA".equals(tipo)) {
                // 3. Si es revista, vamos a la tabla 'revistas' SOLO a buscar la edicion
                int edicion = 0;
                String revRes = SupabaseConfig.get("revistas", "id=eq." + id);
                JsonArray revArr = JsonParser.parseString(revRes).getAsJsonArray();
                if (revArr.size() > 0) {
                    JsonObject revObj = revArr.get(0).getAsJsonObject();
                    if (revObj.has("edicion") && !revObj.get("edicion").isJsonNull()) {
                        edicion = revObj.get("edicion").getAsInt();
                    }
                }
                recursos.add(new Revista(id, titulo, autor, editorial, genero, copias, edicion));
            }
        }
        return recursos;
    }

    public void actualizarDisponibles(int id, int disponibles) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("copias_disponibles", disponibles);
        SupabaseConfig.patch("recursos", id, data);
    }

    public void eliminar(int id) throws IOException {
        SupabaseConfig.delete("recursos", id);
    }
}
