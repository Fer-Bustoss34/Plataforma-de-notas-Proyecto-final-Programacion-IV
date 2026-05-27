package Sistema;

import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.actividad;
import Materia.calificacion;
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

    public String registrarAdministrador(String cedula, String nombre, String apellido,
                                          String correo, String contrasena) {
        if (correoExiste(correo))   return "El correo ya está registrado";
        if (cedulaExiste(cedula))   return "La cédula ya está registrada";
        administradores.add(new Administrador(cedula, nombre, apellido, correo, contrasena));
        return "Administrador registrado exitosamente";
    }

    public String registrarDocente(String cedula, String nombre, String apellido,
                                    String correo, String contrasena, String especialidad) {
        if (correoExiste(correo))   return "El correo ya está registrado";
        if (cedulaExiste(cedula))   return "La cédula ya está registrada";
        docentes.add(new Docente(cedula, nombre, apellido, correo, contrasena, especialidad));
        return "Docente registrado exitosamente";
    }

    public String registrarEstudiante(String cedula, String nombre, String apellido,
                                       String correo, String contrasena, int semestre) {
        if (correoExiste(correo))   return "El correo ya está registrado";
        if (cedulaExiste(cedula))   return "La cédula ya está registrada";
        estudiantes.add(new Estudiante(cedula, nombre, apellido, correo, contrasena, semestre));
        return "Estudiante registrado exitosamente";
    }

    public String eliminarEstudiante(String cedula) {
        boolean eliminado = estudiantes.removeIf(e -> e.getCedula().equals(cedula));
        return eliminado ? "Estudiante eliminado" : "Estudiante no encontrado";
    }

    public String eliminarDocente(String cedula) {
        boolean eliminado = docentes.removeIf(d -> d.getCedula().equals(cedula));
        return eliminado ? "Docente eliminado" : "Docente no encontrado";
    }

    // ── Gestión de materias ──────────────────────────────────────────────────

    public String registrarMateria(String codigo, int creditos, String nombre, Docente docente) {
        if (buscarMateria(codigo) != null)
            return "Ya existe una materia con ese código";

        materia nueva = new materia(codigo, creditos, nombre, docente);
        materias.add(nueva);
        docente.getMaterias().add(nueva);
        return "Materia registrada exitosamente";
    }

    public String eliminarMateria(String codigo) {
        materia objetivo = buscarMateria(codigo);
        if (objetivo == null) return "Materia no encontrada";
        objetivo.getDocente().getMaterias().remove(objetivo);
        materias.remove(objetivo);
        return "Materia eliminada exitosamente";
    }

    // ── Matricular estudiante ────────────────────────────────────────────────

    public String matricularEstudiante(Estudiante e, materia m) {
        if (!estudiantes.contains(e)) return "Estudiante no registrado en el sistema";
        if (!materias.contains(m))    return "Materia no registrada en el sistema";
        return e.matricular(m);
    }

    // ── Calificaciones ───────────────────────────────────────────────────────

    public String registrarCalificacion(materia m, Estudiante e, actividad a, double valor) {
        if (!materias.contains(m))        return "Materia no registrada en el sistema";
        if (!m.getEstudiantes().contains(e)) return "El estudiante no está matriculado en esta materia";

        // Si ya existe la calificación, la actualiza
        for (calificacion c : m.getCalificaciones()) {
            if (c.getEstudiante().getCedula().equals(e.getCedula()) &&
                c.getActividad().getNombre().equals(a.getNombre())) {
                c.setValor(valor);
                return "Calificación actualizada exitosamente";
            }
        }

        // Si no existe, delega al docente de la materia
        return m.getDocente().calificar_estudiante(m, e, a, valor);
    }

    public double calcularNotaFinal(materia m, Estudiante e) {
        double total = 0.0;
        for (calificacion c : m.getCalificaciones())
            if (c.getEstudiante().getCedula().equals(e.getCedula()))
                total += c.getValor() * (c.getActividad().getPorcentaje() / 100.0);
        return Math.round(total * 100.0) / 100.0;
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
