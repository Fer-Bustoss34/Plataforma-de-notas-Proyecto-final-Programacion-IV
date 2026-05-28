package Sistema;

import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.actividad;
import Materia.calificacion;
import Materia.materia;
import User.user;
import DB.Repositorio;
import java.util.ArrayList;

public class Sistema {

    private ArrayList<Administrador> administradores;
    private ArrayList<Docente>       docentes;
    private ArrayList<Estudiante>    estudiantes;
    private ArrayList<materia>       materias;
    private Repositorio              repo;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Sistema() {
        this.administradores = new ArrayList<>();
        this.docentes        = new ArrayList<>();
        this.estudiantes     = new ArrayList<>();
        this.materias        = new ArrayList<>();
        this.repo            = new Repositorio();
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
        repo.guardarDocente(nuevo);
        return true;
    }

    public boolean registrarEstudiante(Estudiante nuevo) {
        if (correoExiste(nuevo.getCorreo())) return false;
        if (cedulaExiste(nuevo.getCedula())) return false;

        estudiantes.add(nuevo);
        repo.guardarEstudiante(nuevo);
        return true;
    }

    public boolean registrarAdministrador(String cedula, String nombre, String apellido,
                                          String correo, String contrasena) {
        if (correoExiste(correo)) return false;
        if (cedulaExiste(cedula)) return false;

        Administrador nuevo = new Administrador(cedula, nombre, apellido, correo, contrasena);
        administradores.add(nuevo);
        repo.guardarAdministrador(nuevo);
        return true;
    }

    public boolean eliminarEstudiante(String cedula) {
        boolean eliminado = estudiantes.removeIf(e -> e.getCedula().equals(cedula));
        if (eliminado) repo.eliminarUsuario(cedula);
        return eliminado;
    }

    public boolean eliminarDocente(String cedula) {
        boolean eliminado = docentes.removeIf(d -> d.getCedula().equals(cedula));
        if (eliminado) repo.eliminarUsuario(cedula);
        return eliminado;
    }

    // ── Gestión de materias ──────────────────────────────────────────────────

    public boolean registrarMateria(materia nueva) {
        if (buscarMateria(nueva.getCodigoMateria()) != null) return false;

        materias.add(nueva);
        nueva.getDocente().getMaterias().add(nueva);
        repo.guardarMateria(nueva);
        return true;
    }

    public boolean eliminarMateria(String codigo) {
        materia objetivo = buscarMateria(codigo);
        if (objetivo == null) return false;

        objetivo.getDocente().getMaterias().remove(objetivo);
        materias.remove(objetivo);
        repo.eliminarMateria(codigo);
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

        String resultado = e.matricular(m);
        if (resultado.startsWith("Matrícula exitosa"))
            repo.guardarMatricula(e.getCedula(), m.getCodigoMateria());
        return resultado;
    }

    // ── Calificaciones ───────────────────────────────────────────────────────

    public boolean registrarCalificacion(materia m, Estudiante e, actividad a, double valor) {
        if (!materias.contains(m))           return false;
        if (!m.getEstudiantes().contains(e)) return false;
        if (valor < 0.0 || valor > 5.0)      return false;

        for (var c : m.getCalificaciones()) {
            if (c.getEstudiante().getCedula().equals(e.getCedula()) && c.getActividad() == a) {
                c.setValor(valor);
                repo.guardarCalificacion(c, m.getCodigoMateria());
                return true;
            }
        }

        calificacion nueva = new calificacion(e, a, valor);
        m.getCalificaciones().add(nueva);
        repo.guardarCalificacion(nueva, m.getCodigoMateria());
        return true;
    }

    public String crearActividad(Docente doc, String titulo, double porciento, materia m) {
        if (!materias.contains(m)) return "Materia no registrada en el sistema";

        String resultado = doc.crear_actividad(titulo, porciento, m);
        if (resultado.equals("Actividad creada exitosamente")) {
            // La actividad recién agregada es la última de la lista
            actividad nueva = m.getActividades().get(m.getActividades().size() - 1);
            repo.guardarActividad(nueva, m.getCodigoMateria());
        }
        return resultado;
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

    // ── Getters ──────────────────────────────────────────────────────────────

    public ArrayList<Administrador> getAdministradores() { return administradores; }
    public ArrayList<Docente>       getDocentes()        { return docentes; }
    public ArrayList<Estudiante>    getEstudiantes()     { return estudiantes; }
    public ArrayList<materia>       getMaterias()        { return materias; }
    public Repositorio              getRepo()            { return repo; }

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