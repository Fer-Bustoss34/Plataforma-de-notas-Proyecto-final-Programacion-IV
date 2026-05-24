package Sistema;

import java.util.ArrayList;
import User.user;
import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
import Materia.actividad;
import Materia.calificacion;

public class Sistema {

    private ArrayList<Administrador> administradores;
    private ArrayList<Docente>       docentes;
    private ArrayList<Estudiante>    estudiantes;
    private ArrayList<materia>       materias;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Sistema() {
        this.administradores = new ArrayList<>();
        this.docentes        = new ArrayList<>();
        this.estudiantes     = new ArrayList<>();
        this.materias        = new ArrayList<>();
        cargarDatosIniciales();
    }

    // ── Autenticación ────────────────────────────────────────────────────────

    /**
     * Busca un usuario por correo y contraseña en los tres roles.
     * Retorna el user si existe, null si no.
     */
    public user login(String correo, String contrasena) {
        for (Administrador a : administradores)
            if (a.getCorreo().equals(correo) && a.getContrasena().equals(contrasena))
                return a;

        for (Docente d : docentes)
            if (d.getCorreo().equals(correo) && d.getContrasena().equals(contrasena))
                return d;

        for (Estudiante e : estudiantes)
            if (e.getCorreo().equals(correo) && e.getContrasena().equals(contrasena))
                return e;

        return null; // credenciales inválidas
    }

    // ── Gestión de usuarios ──────────────────────────────────────────────────

    public boolean registrarAdministrador(Administrador a) {
        if (correoExiste(a.getCorreo())) return false;
        administradores.add(a);
        return true;
    }

    public boolean registrarDocente(Docente d) {
        if (correoExiste(d.getCorreo())) return false;
        docentes.add(d);
        return true;
    }

    public boolean registrarEstudiante(Estudiante e) {
        if (correoExiste(e.getCorreo())) return false;
        estudiantes.add(e);
        return true;
    }

    public boolean eliminarEstudiante(String cedula) {
        return estudiantes.removeIf(e -> e.getCedula().equals(cedula));
    }

    public boolean eliminarDocente(String cedula) {
        return docentes.removeIf(d -> d.getCedula().equals(cedula));
    }

    // ── Gestión de materias ──────────────────────────────────────────────────

    public boolean registrarMateria(materia m) {
        for (materia existente : materias)
            if (existente.getCodigoMateria().equals(m.getCodigoMateria()))
                return false; // código duplicado
        materias.add(m);
        m.getDocente().getMaterias().add(m); // sincroniza con el docente
        return true;
    }

    public boolean eliminarMateria(String codigo) {
        materia objetivo = buscarMateria(codigo);
        if (objetivo == null) return false;
        objetivo.getDocente().getMaterias().remove(objetivo);
        return materias.remove(objetivo);
    }

    // ── Calificaciones ───────────────────────────────────────────────────────

    /**
     * Registra una nota. Valida que el estudiante esté matriculado
     * y que el valor esté en rango 0-5.
     */
    public boolean registrarCalificacion(materia m, Estudiante est,
                                         actividad act, double valor) {
        if (valor < 0.0 || valor > 5.0) {
            System.out.println("Error: la nota debe estar entre 0.0 y 5.0");
            return false;
        }
        if (!m.getEstudiantes().contains(est)) {
            System.out.println("Error: el estudiante no está matriculado en esta materia.");
            return false;
        }
        // Evitar duplicado (mismo estudiante + misma actividad)
        for (calificacion c : m.getCalificaciones())
            if (c.getEstudiante().equals(est) && c.getActividad().equals(act)) {
                c.setValor(valor); // actualiza si ya existe
                return true;
            }
        m.getCalificaciones().add(new calificacion(est, act, valor));
        return true;
    }

    /**
     * Calcula la nota definitiva de un estudiante en una materia.
     * Suma (valor * porcentaje / 100) por cada calificación.
     */
    public double calcularNotaFinal(materia m, Estudiante est) {
        double total = 0.0;
        for (calificacion c : m.getCalificaciones())
            if (c.getEstudiante().equals(est))
                total += c.getValor() * (c.getActividad().getPorcentaje() / 100.0);
        return Math.round(total * 100.0) / 100.0; // redondea a 2 decimales
    }

    // ── Búsquedas ────────────────────────────────────────────────────────────

    public Estudiante buscarEstudiante(String cedula) {
        for (Estudiante e : estudiantes)
            if (e.getCedula().equals(cedula)) return e;
        return null;
    }

    public Docente buscarDocente(String cedula) {
        for (Docente d : docentes)
            if (d.getCedula().equals(cedula)) return d;
        return null;
    }

    public materia buscarMateria(String codigo) {
        for (materia m : materias)
            if (m.getCodigoMateria().equals(codigo)) return m;
        return null;
    }

    // ── Listados ─────────────────────────────────────────────────────────────

    public void listarEstudiantes() {
        if (estudiantes.isEmpty()) { System.out.println("No hay estudiantes registrados."); return; }
        estudiantes.forEach(System.out::println);
    }

    public void listarDocentes() {
        if (docentes.isEmpty()) { System.out.println("No hay docentes registrados."); return; }
        docentes.forEach(System.out::println);
    }

    public void listarMaterias() {
        if (materias.isEmpty()) { System.out.println("No hay materias registradas."); return; }
        materias.forEach(System.out::println);
    }

    // ── Getters de listas ────────────────────────────────────────────────────

    public ArrayList<Administrador> getAdministradores() { return administradores; }
    public ArrayList<Docente>       getDocentes()        { return docentes; }
    public ArrayList<Estudiante>    getEstudiantes()     { return estudiantes; }
    public ArrayList<materia>       getMaterias()        { return materias; }

    // ── Utilidades privadas ──────────────────────────────────────────────────

    private boolean correoExiste(String correo) {
        for (Administrador a : administradores) if (a.getCorreo().equals(correo)) return true;
        for (Docente d       : docentes)        if (d.getCorreo().equals(correo)) return true;
        for (Estudiante e    : estudiantes)      if (e.getCorreo().equals(correo)) return true;
        return false;
    }

    /** Datos de prueba para arrancar la app */
    private void cargarDatosIniciales() {
        Administrador admin = new Administrador("000", "Admin", "Sistema",
                                                "admin@uni.edu", "admin123");
        administradores.add(admin);

        Docente doc = new Docente("111", "Carlos", "Pérez",
                                  "cperez@uni.edu", "doc123",
                                  "D-001", "Ingeniería de Software");
        docentes.add(doc);

        Estudiante est = new Estudiante("222", "Ana", "Gómez",
                                        "agomez@uni.edu", "est123", 3);
        estudiantes.add(est);

        materia mat = new materia("MAT-101", 3, "Programación I", doc);
        materias.add(mat);
        doc.getMaterias().add(mat);
    }
}