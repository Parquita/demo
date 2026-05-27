package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class BibliotecaController {

    private Biblioteca biblioteca;

    public BibliotecaController() {
        this.biblioteca = new Biblioteca();
    }

    @GetMapping("/recursos")
    public List<Recurso> obtenerCatalogo() {
        try {
            return biblioteca.getRecursos();
        } catch (Exception e) {
            System.err.println("Error al obtener catálogo: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/recursos/libro")
    public ResponseEntity<String> agregarLibro(@RequestBody java.util.Map<String, Object> payload) {
        try {
            String titulo = payload.get("titulo").toString();
            String autor = payload.get("autor").toString();
            String editorial = payload.get("editorial").toString();
            String genero = payload.get("genero").toString();
            int copias = Integer.parseInt(payload.get("copias").toString());
            int paginas = Integer.parseInt(payload.get("numeroPaginas").toString());

            biblioteca.registrarLibro(titulo, autor, editorial, genero, copias, paginas);
            
            return ResponseEntity.ok("Libro registrado exitosamente");
        } catch (Exception e) {
            System.err.println("Error al registrar libro: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al registrar libro");
        }
    }

    @PostMapping("/recursos/revista")
    public ResponseEntity<String> agregarRevista(@RequestBody java.util.Map<String, Object> payload) {
        try {
            String titulo = payload.get("titulo").toString();
            String autor = payload.get("autor").toString();
            String editorial = payload.get("editorial").toString();
            String genero = payload.get("genero").toString();
            int copias = Integer.parseInt(payload.get("copias").toString());
            int edicion = Integer.parseInt(payload.get("edicion").toString());

            biblioteca.registrarRevista(titulo, autor, editorial, genero, copias, edicion);
            
            return ResponseEntity.ok("Revista registrada exitosamente");
        } catch (Exception e) {
            System.err.println("Error al registrar revista: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al registrar revista");
        }
    }

    @DeleteMapping("/recursos/{id}")
    public ResponseEntity<String> eliminarRecurso1(@PathVariable int id) {
        try {
            biblioteca.eliminarRecurso(id);
            return ResponseEntity.ok("Recurso eliminado");
        } catch (Exception e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al eliminar");
        }
    }

    @PutMapping("/recursos/{id}")
    public ResponseEntity<String> actualizarRecurso(@PathVariable int id, @RequestBody java.util.Map<String, Object> payload) {
        try {
            biblioteca.actualizarRecurso(id, payload);
            return ResponseEntity.ok("Recurso actualizado");
        } catch (Exception e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al actualizar");
        }
    }


    @PostMapping("/prestamos")
    public ResponseEntity<String> solicitarPrestamo(@RequestBody java.util.Map<String, Object> payload) {
        try {
            // Validaciones básicas
            if (!payload.containsKey("idRecurso") || payload.get("idRecurso").toString().isEmpty() ||
                !payload.containsKey("idUsuario") || payload.get("idUsuario").toString().isEmpty() ||
                !payload.containsKey("contraseña")) {
                return ResponseEntity.badRequest().body("Faltan datos obligatorios para el préstamo.");
            }

            int idRecurso = Integer.parseInt(payload.get("idRecurso").toString());
            int idUsuario = Integer.parseInt(payload.get("idUsuario").toString()); 
            String fechaDevolucion = payload.get("fechaDevolucion").toString();
            String contrasena = payload.get("contraseña").toString();

            String resultado = biblioteca.prestar(idRecurso, idUsuario, fechaDevolucion, contrasena);
            
            if ("EXITO".equals(resultado)) {
                return ResponseEntity.ok("Préstamo registrado exitosamente");
            } else {
                return ResponseEntity.badRequest().body(resultado); 
            }
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Error de formato numérico en los IDs.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    @GetMapping("/prestamos/mis-prestamos")
    public ResponseEntity<String> verMisPrestamos(@RequestParam int idUsuario) {
        try {
            String datos = biblioteca.obtenerMisPrestamos(idUsuario);
            return ResponseEntity.ok(datos); 
        } catch (Exception e) {
            System.err.println("Error al cargar mis préstamos: " + e.getMessage());
            return ResponseEntity.internalServerError().body("[]");
        }
    }

    @PutMapping("/prestamos/{idPrestamo}/devolver")
    public ResponseEntity<String> devolverPrestamo(@PathVariable int idPrestamo) {
        try {
            com.google.gson.JsonObject updateData = new com.google.gson.JsonObject();
            updateData.addProperty("devuelto", true);
            
            SupabaseConfig.patch("prestamos", idPrestamo, updateData);
            
            return ResponseEntity.ok("Devolución registrada");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno");
        }
    }


    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody java.util.Map<String, String> payload) {
        try {
            String usuario = payload.get("usuario");
            String correo = payload.get("correo");
            String contrasena = payload.get("contraseña"); 

            boolean exito = biblioteca.registrarUsuario(usuario, correo, contrasena);
            
            if (exito) {
                return ResponseEntity.ok("Usuario registrado exitosamente");
            } else {
                return ResponseEntity.badRequest().body("Error: El correo ya está registrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno al registrar usuario");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(@RequestBody java.util.Map<String, String> payload) {
        try {
            String correo = payload.get("correo");
            String contrasena = payload.get("contraseña");

            String userData = biblioteca.iniciarSesion(correo, contrasena);
            
            if (userData != null) {
                return ResponseEntity.ok(userData);
            } else {
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor durante el login");
        }
    }
    @GetMapping("/prestamos/todos")
    public ResponseEntity<String> verTodosLosPrestamos() {
        try {
            String datos = biblioteca.obtenerTodosLosPrestamos();
            return ResponseEntity.ok(datos); 
        } catch (Exception e) {
            System.err.println("Error al cargar todos los préstamos: " + e.getMessage());
            return ResponseEntity.internalServerError().body("[]");
        }
    }
}

