package com.example.demo;
/**
 * Clase para hacer pruebas manuales y agregar datos a la base de datos
 * Ejecuta diferentes escenarios de prueba
 */
public class TestPruebas {
    public static void main(String[] args) {
        try {
            Biblioteca biblioteca = new Biblioteca();
            
            System.out.println("🧪 === PRUEBAS MANUALES DE BIBLIOTECA ===\n");
            
            // ========== PRUEBA 1: AGREGAR USUARIOS ==========
            System.out.println("1️⃣  AGREGANDO USUARIOS...");
            agregarUsuarios(biblioteca);
            
            // ========== PRUEBA 2: AGREGAR LIBROS ==========
            System.out.println("\n2️⃣  AGREGANDO LIBROS...");
            agregarLibros(biblioteca);
            
            // ========== PRUEBA 3: AGREGAR REVISTAS ==========
            System.out.println("\n3️⃣  AGREGANDO REVISTAS...");
            agregarRevistas(biblioteca);
            
            // ========== PRUEBA 4: VER TODOS LOS DATOS ==========
            System.out.println("\n4️⃣  DATOS ACTUALES EN LA BASE:");
            mostrarDatos(biblioteca);
            
            // ========== PRUEBA 5: HACER UN PRÉSTAMO ==========
            System.out.println("\n5️⃣  HACIENDO UN PRÉSTAMO...");
            hacerPrestamos(biblioteca);
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Agrega usuarios de prueba a la base de datos
     */
    private static void agregarUsuarios(Biblioteca biblioteca) {
        String[] usuarios = {
            "juan,juan@email.com,pass123",
            "ana,ana@email.com,pass456",
            "pedro,pedro@email.com,pass789",
            "laura,laura@email.com,pass000",
        };
        
        for (String userData : usuarios) {
            String[] datos = userData.split(",");
            try {
                biblioteca.registrarUsuario(datos[0], datos[1], datos[2]);
                System.out.println("  ✓ Usuario '" + datos[0] + "' agregado");
            } catch (Exception e) {
                System.out.println("  ⚠ Usuario '" + datos[0] + "' ya existe");
            }
        }
    }
    
    /**
     * Agrega libros de prueba
     */
    private static void agregarLibros(Biblioteca biblioteca) {
        Object[][] libros = {
            {"El Quijote", "Miguel de Cervantes", "Clásicos Modernos", "Novela", 5, 895},
            {"Fundación", "Isaac Asimov", "Gnomon Editores", "Ciencia Ficción", 3, 428},
            {"1984", "George Orwell", "Signet", "Distopía", 4, 328},
            {"Cien años de soledad", "Gabriel García Márquez", "Sudamericana", "Realismo Mágico", 6, 431},
            {"El Código Da Vinci", "Dan Brown", "Doubleday", "Misterio", 2, 689},
            {"Harry Potter y la Piedra Filosofal", "J.K. Rowling", "Bloomsbury", "Fantasía", 8, 309},
        };
        
        for (Object[] libro : libros) {
            try {
                biblioteca.registrarLibro(
                    (String) libro[0],
                    (String) libro[1],
                    (String) libro[2],
                    (String) libro[3],
                    (Integer) libro[4],
                    (Integer) libro[5]
                );
                System.out.println("  ✓ Libro '" + libro[0] + "' agregado");
            } catch (Exception e) {
                System.out.println("  ⚠ Libro '" + libro[0] + "' ya existe");
            }
        }
    }
    
    /**
     * Agrega revistas de prueba
     */
    private static void agregarRevistas(Biblioteca biblioteca) {
        Object[][] revistas = {
            {"National Geographic", "Varios", "National Geographic Society", "Divulgación", 10, 45},
            {"Scientific American", "Varios", "Springer", "Ciencia", 7, 156},
            {"Vogue", "Varios", "Condé Nast", "Moda", 5, 89},
        };
        
        for (Object[] revista : revistas) {
            try {
                biblioteca.registrarRevista(
                    (String) revista[0],
                    (String) revista[1],
                    (String) revista[2],
                    (String) revista[3],
                    (Integer) revista[4],
                    (Integer) revista[5]
                );
                System.out.println("  ✓ Revista '" + revista[0] + "' agregada");
            } catch (Exception e) {
                System.out.println("  ⚠ Revista '" + revista[0] + "' ya existe");
            }
        }
    }
    
    /**
     * Muestra todos los datos actuales
     */
    private static void mostrarDatos(Biblioteca biblioteca) {
        try {
            System.out.println("\n📊 ESTADÍSTICAS:");
            System.out.println("  • Total usuarios: " + biblioteca.totalUsuarios());
            System.out.println("  • Total recursos: " + biblioteca.totalRecursos());
            System.out.println("  • Total préstamos: " + biblioteca.getPrestamos().size());
            
        } catch (Exception e) {
            System.err.println("  ❌ Error al obtener datos: " + e.getMessage());
        }
    }
    
    /**
     * Realiza préstamos de prueba
     */
    private static void hacerPrestamos(Biblioteca biblioteca) {
        try {
            // Obtener el primer usuario e intenta hacer un préstamo
            var usuarios = biblioteca.getUsuarios();
            var recursos = biblioteca.getRecursos();
            
            if (!usuarios.isEmpty() && !recursos.isEmpty()) {
                int idUsuario = usuarios.get(0).getId();
                int idRecurso = recursos.get(0).getId();
                
                try {
                    biblioteca.prestarRecurso(idUsuario, idRecurso, "2026-06-15");
                    System.out.println("  ✓ Préstamo realizado:");
                    System.out.println("    - Usuario ID: " + idUsuario);
                    System.out.println("    - Recurso ID: " + idRecurso);
                    System.out.println("    - Fecha devolución: 2026-06-15");
                } catch (Exception e) {
                    System.out.println("  ⚠ No se pudo hacer el préstamo: " + e.getMessage());
                }
            } else {
                System.out.println("  ⚠ No hay usuarios o recursos para hacer un préstamo");
            }
            
        } catch (Exception e) {
            System.err.println("  ❌ Error en préstamo: " + e.getMessage());
        }
    }
}
