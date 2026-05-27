package com.example.demo;
import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public void insertar(Usuario usuario) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("usuario", usuario.getUsuario());
        data.addProperty("correo", usuario.getCorreo());
        data.addProperty("contraseña", usuario.getContrasena());

        String response = SupabaseConfig.post("usuarios", data);
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            usuario.setId(obj.get("id").getAsInt());
        }
    }

    public Usuario obtenerPorId(int id) throws IOException {
        String query = "id=eq." + id;
        String response = SupabaseConfig.get("usuarios", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            return new Usuario(
                    obj.get("id").getAsInt(),
                    obj.get("usuario").getAsString(),
                    obj.get("correo").getAsString(),
                    obj.get("contraseña").getAsString()
            );
        }
        return null;
    }

    public Usuario obtenerPorCorreo(String correo) throws IOException {
        String query = "correo=eq." + correo;
        String response = SupabaseConfig.get("usuarios", query);
        
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        if (array.size() > 0) {
            JsonObject obj = array.get(0).getAsJsonObject();
            return new Usuario(
                    obj.get("id").getAsInt(),
                    obj.get("usuario").getAsString(),
                    obj.get("correo").getAsString(),
                    obj.get("contraseña").getAsString()
            );
        }
        return null;
    }

    public List<Usuario> obtenerTodos() throws IOException {
        String response = SupabaseConfig.get("usuarios", "select=*");
        List<Usuario> usuarios = new ArrayList<>();

        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            usuarios.add(new Usuario(
                    obj.get("id").getAsInt(),
                    obj.get("usuario").getAsString(),
                    obj.get("correo").getAsString(),
                    obj.get("contraseña").getAsString()
            ));
        }
        return usuarios;
    }

    public void actualizar(Usuario usuario) throws IOException {
        JsonObject data = new JsonObject();
        data.addProperty("usuario", usuario.getUsuario());
        data.addProperty("correo", usuario.getCorreo());
        data.addProperty("contraseña", usuario.getContrasena());

        SupabaseConfig.patch("usuarios", usuario.getId(), data);
    }

    public void eliminar(int id) throws IOException {
        SupabaseConfig.delete("usuarios", id);
    }
}
