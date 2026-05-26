package Menu;

import java.util.Scanner;
import Sistema.Sistema;
import User.user;
import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
import Materia.actividad;
import Materia.calificacion;

public class Menu {

    private final Sistema  sistema;
    private final Scanner  sc;

    public Menu(Sistema sistema) {
        this.sistema = sistema;
        this.sc      = new Scanner(System.in);
    }

    // ── Login ────────────────────────────────────────────────────────────────

    public void iniciar() {
        System.out.println("=== Bienvenido al Sistema Académico ===");
        boolean ejecutando = true;
        while (ejecutando) {
            System.out.print("\nCorreo: ");    String correo     = sc.nextLine().trim();
            System.out.print("Contraseña: "); String contrasena = sc.nextLine().trim();

            user usuario = sistema.login(correo, contrasena);
            if (usuario == null) {
                System.out.println("Credenciales incorrectas. Intente de nuevo.");
                continue;
            }

            System.out.println("\nBienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");

            switch (usuario.getRol()) {
                case "Administrador" -> ejecutando = !menuAdmin((Administrador) usuario);
                case "Docente"       -> ejecutando = !menuDocente((Docente) usuario);
                case "Estudiante"    -> ejecutando = !menuEstudiante((Estudiante) usuario);
            }
        }
        System.out.println("Hasta luego.");
        sc.close();
    }

    // ── Menú Administrador ───────────────────────────────────────────────────

    private boolean menuAdmin(Administrador admin) {
        int opc;
        do {
            System.out.println("""

                === Menú Administrador ===
                1. Registrar docente
                2. Registrar estudiante
                3. Registrar materia
                4. Listar docentes
                5. Listar estudiantes
                6. Listar materias
                7. Eliminar estudiante
                8. Eliminar docente
                0. Cerrar sesión""");

            System.out.print("Opción: ");
            opc = leerEntero();

            switch (opc) {
                case 1 -> registrarDocente();
                case 2 -> registrarEstudiante();
                case 3 -> registrarMateria();
                case 4 -> sistema.listarDocentes();
                case 5 -> sistema.listarEstudiantes();
                case 6 -> sistema.listarMaterias();
                case 7 -> eliminarEstudiante();
                case 8 -> eliminarDocente();
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 0);
        return true;
    }

    // ── Menú Docente ─────────────────────────────────────────────────────────

    private boolean menuDocente(Docente docente) {
        int opc;
        do {
            System.out.println("""

                === Menú Docente ===
                1. Ver mis materias
                2. Agregar actividad a una materia
                3. Registrar calificación
                4. Ver calificaciones de una materia
                5. Ver nota final de un estudiante
                0. Cerrar sesión""");

            System.out.print("Opción: ");
            opc = leerEntero();

            switch (opc) {
                case 1 -> docente.getMaterias().forEach(System.out::println);
                case 2 -> agregarActividad(docente);
                case 3 -> registrarCalificacion(docente);
                case 4 -> verCalificaciones(docente);
                case 5 -> verNotaFinal(docente);
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 0);
        return true;
    }

    // ── Menú Estudiante ──────────────────────────────────────────────────────

    private boolean menuEstudiante(Estudiante estudiante) {
        int opc;
        do {
            System.out.println("""

                === Menú Estudiante ===
                1. Ver mis materias
                2. Matricular materia
                3. Ver mis calificaciones
                4. Ver mi nota final por materia
                0. Cerrar sesión""");

            System.out.print("Opción: ");
            opc = leerEntero();

            switch (opc) {
                case 1 -> estudiante.getMaterias().forEach(System.out::println);
                case 2 -> matricularEstudiante(estudiante);
                case 3 -> verMisCalificaciones(estudiante);
                case 4 -> verMiNotaFinal(estudiante);
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 0);
        return true;
    }

    // ── Operaciones de Admin ─────────────────────────────────────────────────

    private void registrarDocente() {
        System.out.print("Cédula: ");       String ced  = sc.nextLine();
        System.out.print("Nombre: ");       String nom  = sc.nextLine();
        System.out.print("Apellido: ");     String ape  = sc.nextLine();
        System.out.print("Correo: ");       String cor  = sc.nextLine();
        System.out.print("Contraseña: ");   String con  = sc.nextLine();
        System.out.print("Código docente: ");String cod  = sc.nextLine();
        System.out.print("Especialidad: "); String esp  = sc.nextLine();

        boolean ok = sistema.registrarDocente(
            new Docente(ced, nom, ape, cor, con, cod, esp));
        System.out.println(ok ? "Docente registrado." : "El correo ya existe.");
    }

    private void registrarEstudiante() {
        System.out.print("Cédula: ");     String ced = sc.nextLine();
        System.out.print("Nombre: ");     String nom = sc.nextLine();
        System.out.print("Apellido: ");   String ape = sc.nextLine();
        System.out.print("Correo: ");     String cor = sc.nextLine();
        System.out.print("Contraseña: "); String con = sc.nextLine();
        System.out.print("Semestre: ");   int    sem = leerEntero();

        boolean ok = sistema.registrarEstudiante(
            new Estudiante(ced, nom, ape, cor, con, sem));
        System.out.println(ok ? "Estudiante registrado." : "El correo ya existe.");
    }

    private void registrarMateria() {
        System.out.print("Código materia: "); String cod  = sc.nextLine();
        System.out.print("Nombre: ");         String nom  = sc.nextLine();
        System.out.print("Créditos: ");       int    cred = leerEntero();
        System.out.print("Cédula del docente: "); String cedDoc = sc.nextLine();

        Docente doc = sistema.buscarDocente(cedDoc);
        if (doc == null) { System.out.println("Docente no encontrado."); return; }

        boolean ok = sistema.registrarMateria(new materia(cod, cred, nom, doc));
        System.out.println(ok ? "Materia registrada." : "El código ya existe.");
    }

    private void eliminarEstudiante() {
        System.out.print("Cédula del estudiante a eliminar: ");
        String ced = sc.nextLine();
        System.out.println(sistema.eliminarEstudiante(ced)
            ? "Estudiante eliminado." : "No encontrado.");
    }

    private void eliminarDocente() {
        System.out.print("Cédula del docente a eliminar: ");
        String ced = sc.nextLine();
        System.out.println(sistema.eliminarDocente(ced)
            ? "Docente eliminado." : "No encontrado.");
    }

    // ── Operaciones de Docente ───────────────────────────────────────────────

    private void agregarActividad(Docente docente) {
        materia m = seleccionarMateriaDocente(docente);
        if (m == null) return;

        System.out.print("Nombre de la actividad: "); String nom = sc.nextLine();
        System.out.print("Porcentaje (ej. 30): ");    double por = leerDouble();

        // Validar que no supere 100%
        double acumulado = m.getActividades().stream()
                            .mapToDouble(actividad::getPorcentaje).sum();
        if (acumulado + por > 100) {
            System.out.printf("Error: ya hay %.1f%% acumulado. No puede agregar %.1f%%.%n",
                              acumulado, por);
            return;
        }
        m.getActividades().add(new actividad(nom, por));
        System.out.printf("Actividad agregada. Total acumulado: %.1f%%%n", acumulado + por);
    }

    private void registrarCalificacion(Docente docente) {
        materia m = seleccionarMateriaDocente(docente);
        if (m == null || m.getEstudiantes().isEmpty()) {
            System.out.println("La materia no tiene estudiantes matriculados.");
            return;
        }
        if (m.getActividades().isEmpty()) {
            System.out.println("La materia no tiene actividades.");
            return;
        }

        System.out.print("Cédula del estudiante: ");
        Estudiante est = sistema.buscarEstudiante(sc.nextLine());
        if (est == null) { System.out.println("Estudiante no encontrado."); return; }

        System.out.println("Actividades:");
        for (int i = 0; i < m.getActividades().size(); i++)
            System.out.println(i + ". " + m.getActividades().get(i));
        System.out.print("Seleccione actividad: ");
        int idx = leerEntero();
        if (idx < 0 || idx >= m.getActividades().size()) {
            System.out.println("Índice inválido."); return;
        }

        System.out.print("Nota (0.0 - 5.0): ");
        double valor = leerDouble();

        boolean ok = sistema.registrarCalificacion(m, est, m.getActividades().get(idx), valor);
        System.out.println(ok ? "Calificación registrada." : "No se pudo registrar.");
    }

    private void verCalificaciones(Docente docente) {
        materia m = seleccionarMateriaDocente(docente);
        if (m == null) return;
        if (m.getCalificaciones().isEmpty())
            System.out.println("Sin calificaciones aún.");
        else
            m.getCalificaciones().forEach(System.out::println);
    }

    private void verNotaFinal(Docente docente) {
        materia m = seleccionarMateriaDocente(docente);
        if (m == null) return;
        System.out.print("Cédula del estudiante: ");
        Estudiante est = sistema.buscarEstudiante(sc.nextLine());
        if (est == null) { System.out.println("Estudiante no encontrado."); return; }
        System.out.printf("Nota final de %s: %.2f%n",
                          est.getNombre(), sistema.calcularNotaFinal(m, est));
    }

    // ── Operaciones de Estudiante ────────────────────────────────────────────

    private void matricularEstudiante(Estudiante est) {
        if (sistema.getMaterias().isEmpty()) {
            System.out.println("No hay materias disponibles."); return;
        }
        System.out.println("Materias disponibles:");
        for (int i = 0; i < sistema.getMaterias().size(); i++)
            System.out.println(i + ". " + sistema.getMaterias().get(i).getNombre()
                               + " — " + sistema.getMaterias().get(i).getCodigoMateria());

        System.out.print("Seleccione: ");
        int idx = leerEntero();
        if (idx < 0 || idx >= sistema.getMaterias().size()) {
            System.out.println("Opción inválida."); return;
        }
        materia seleccionada = sistema.getMaterias().get(idx);
        if (est.getMaterias().contains(seleccionada)) {
            System.out.println("Ya estás matriculado en esa materia."); return;
        }
        est.getMaterias().add(seleccionada);
        seleccionada.agregarEstudiante(est);
        System.out.println("Matriculado en: " + seleccionada.getNombre());
    }

    private void verMisCalificaciones(Estudiante est) {
        boolean hayNotas = false;
        for (materia m : est.getMaterias()) {
            for (calificacion c : m.getCalificaciones()) {  // necesita import
                if (c.getEstudiante().equals(est)) {
                    System.out.println("[" + m.getNombre() + "] " + c);
                    hayNotas = true;
                }
            }
        }
        if (!hayNotas) System.out.println("No tienes calificaciones aún.");
    }

    private void verMiNotaFinal(Estudiante est) {
        if (est.getMaterias().isEmpty()) { System.out.println("No tienes materias."); return; }
        for (materia m : est.getMaterias())
            System.out.printf("%-25s → %.2f%n",
                              m.getNombre(), sistema.calcularNotaFinal(m, est));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private materia seleccionarMateriaDocente(Docente docente) {
        if (docente.getMaterias().isEmpty()) {
            System.out.println("No tienes materias asignadas."); return null;
        }
        for (int i = 0; i < docente.getMaterias().size(); i++)
            System.out.println(i + ". " + docente.getMaterias().get(i).getNombre());
        System.out.print("Seleccione materia: ");
        int idx = leerEntero();
        if (idx < 0 || idx >= docente.getMaterias().size()) {
            System.out.println("Índice inválido."); return null;
        }
        return docente.getMaterias().get(idx);
    }

    private int leerEntero() {
        while (!sc.hasNextInt()) { System.out.print("Ingrese un número: "); sc.next(); }
        int n = sc.nextInt(); sc.nextLine(); return n;
    }

    private double leerDouble() {
        while (!sc.hasNextDouble()) { System.out.print("Ingrese un número: "); sc.next(); }
        double n = sc.nextDouble(); sc.nextLine(); return n;
    }
}