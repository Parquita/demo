package com.example.demo;

import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.io.IOException;

public class SupabaseConfig {
    private static final String SUPABASE_URL = "https://unaeutnqgaxvqavwyorp.supabase.co/rest/v1/";
    private static final String API_KEY = "sb_publishable_1v2Czp3zRu9xC-Eigu29Zg_xzyoF01G";
    
    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public static String get(String table, String query) throws IOException {
        String url = SUPABASE_URL + table + "?" + query;
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en GET: " + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

    public static String post(String table, JsonObject data) throws IOException {
        String url = SUPABASE_URL + table;
        String jsonBody = gson.toJson(data);

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=representation")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en POST: " + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

    public static String patch(String table, int id, JsonObject data) throws IOException {
        String url = SUPABASE_URL + table + "?id=eq." + id;
        String jsonBody = gson.toJson(data);

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en PATCH: " + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

    public static String delete(String table, int id) throws IOException {
        String url = SUPABASE_URL + table + "?id=eq." + id;
        
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en DELETE: " + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

    public static Gson getGson() {
        return gson;
    }
}
