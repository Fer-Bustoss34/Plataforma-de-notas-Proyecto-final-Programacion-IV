package Sistema;

import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.actividad;
import Materia.materia;
import User.user;
import java.util.ArrayList;

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
    }

    // ── Autenticación ────────────────────────────────────────────────────────

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

        return null;
    }

    // ── Gestión de usuarios ──────────────────────────────────────────────────

    public boolean registrarDocente(Docente nuevo) {
        if (correoExiste(nuevo.getCorreo())) return false;
        if (cedulaExiste(nuevo.getCedula())) return false;

        docentes.add(nuevo);
        return true;
    }

    public boolean registrarEstudiante(Estudiante nuevo) {
        if (correoExiste(nuevo.getCorreo())) return false;
        if (cedulaExiste(nuevo.getCedula())) return false;

        estudiantes.add(nuevo);
        return true;
    }

    public boolean registrarAdministrador(String cedula, String nombre, String apellido,
                                          String correo, String contrasena) {
        if (correoExiste(correo)) return false;
        if (cedulaExiste(cedula)) return false;

        administradores.add(new Administrador(cedula, nombre, apellido, correo, contrasena));
        return true;
    }

    public boolean eliminarEstudiante(String cedula) {
        return estudiantes.removeIf(e -> e.getCedula().equals(cedula));
    }

    public boolean eliminarDocente(String cedula) {
        return docentes.removeIf(d -> d.getCedula().equals(cedula));
    }

    // ── Gestión de materias ──────────────────────────────────────────────────

    public boolean registrarMateria(materia nueva) {
        if (buscarMateria(nueva.getCodigoMateria()) != null) return false;

        materias.add(nueva);
        nueva.getDocente().getMaterias().add(nueva);
        return true;
    }

    public boolean eliminarMateria(String codigo) {
        materia objetivo = buscarMateria(codigo);
        if (objetivo == null) return false;

        objetivo.getDocente().getMaterias().remove(objetivo);
        materias.remove(objetivo);
        return true;
    }

    // ── Asignar docente a materia ────────────────────────────────────────────

    public String asignarDocente(Administrador admin, materia m, Docente docente) {
        if (!materias.contains(m))       return "Materia no registrada en el sistema";
        if (!docentes.contains(docente)) return "Docente no registrado en el sistema";

        ArrayList<Docente> lista = new ArrayList<>();
        lista.add(docente);
        return admin.asignar_docente(0, lista, m);
    }

    // ── Matricular estudiante ────────────────────────────────────────────────

    public String matricularEstudiante(Estudiante e, materia m) {
        if (!estudiantes.contains(e)) return "Estudiante no registrado en el sistema";
        if (!materias.contains(m))    return "Materia no registrada en el sistema";

        return e.matricular(m);
    }

    // ── Calificaciones ───────────────────────────────────────────────────────

    public boolean registrarCalificacion(materia m, Estudiante e, actividad a, double valor) {
        if (!materias.contains(m))           return false;
        if (!m.getEstudiantes().contains(e)) return false;
        if (valor < 0.0 || valor > 5.0)      return false;

        // Si ya existe una calificación para este estudiante y actividad, actualiza el valor
        for (var c : m.getCalificaciones()) {
            if (c.getEstudiante().getCedula().equals(e.getCedula())
                    && c.getActividad() == a) {
                c.setValor(valor);
                return true;
            }
        }

        m.getCalificaciones().add(new Materia.calificacion(e, a, valor));
        return true;
    }

    public String crearActividad(Docente doc, String titulo, double porciento, materia m) {
        if (!materias.contains(m)) return "Materia no registrada en el sistema";

        return doc.crear_actividad(titulo, porciento, m);
    }

    public double calcularNotaFinal(materia m, Estudiante e) {
        double total = 0.0;
        for (var c : m.getCalificaciones())
            if (c.getEstudiante().getCedula().equals(e.getCedula()))
                total += c.getValor() * (c.getActividad().getPorcentaje() / 100.0);
        return Math.round(total * 100.0) / 100.0;
    }

    public double promedio_materia(Estudiante e, materia m) {
        if (!materias.contains(m)) return -1;
        return e.promedio_materia(m);
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

    // ── Getters de listas ────────────────────────────────────────────────────

    public ArrayList<Administrador> getAdministradores() { return administradores; }
    public ArrayList<Docente>       getDocentes()        { return docentes; }
    public ArrayList<Estudiante>    getEstudiantes()     { return estudiantes; }
    public ArrayList<materia>       getMaterias()        { return materias; }

    // ── Utilidades privadas ──────────────────────────────────────────────────

    private boolean correoExiste(String correo) {
        for (Administrador a : administradores) if (a.getCorreo().equals(correo)) return true;
        for (Docente d       : docentes)        if (d.getCorreo().equals(correo)) return true;
        for (Estudiante e    : estudiantes)     if (e.getCorreo().equals(correo)) return true;
        return false;
    }

    private boolean cedulaExiste(String cedula) {
        for (Administrador a : administradores) if (a.getCedula().equals(cedula)) return true;
        for (Docente d       : docentes)        if (d.getCedula().equals(cedula)) return true;
        for (Estudiante e    : estudiantes)     if (e.getCedula().equals(cedula)) return true;
        return false;
    }
}