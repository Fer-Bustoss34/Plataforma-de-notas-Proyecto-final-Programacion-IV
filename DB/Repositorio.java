package DB;

import java.sql.*;
import Sistema.Sistema;
import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.actividad;
import Materia.calificacion;
import Materia.materia;

public class Repositorio {

    // ── Cargar todo al arrancar ───────────────────────────────────────────────

    public void cargarTodo(Sistema sistema) {
        cargarAdministradores(sistema);
        cargarDocentes(sistema);
        cargarEstudiantes(sistema);
        cargarMaterias(sistema);
        cargarActividades(sistema);
        cargarMatriculas(sistema);
        cargarCalificaciones(sistema);
        System.out.println("Datos cargados desde Supabase");
    }

    private void cargarAdministradores(Sistema sistema) {
        String sql = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.contrasena " +
                     "FROM usuarios u JOIN administradores a ON u.cedula = a.cedula";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sistema.registrarAdministrador(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("contrasena")
                );
            }
        } catch (Exception e) {
            System.out.println("Error cargando administradores: " + e.getMessage());
        }
    }

    private void cargarDocentes(Sistema sistema) {
        String sql = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.contrasena, d.especialidad " +
                     "FROM usuarios u JOIN docentes d ON u.cedula = d.cedula";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Docente nuevo = new Docente(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("especialidad")
                );
                sistema.registrarDocente(nuevo);
            }
        } catch (Exception e) {
            System.out.println("Error cargando docentes: " + e.getMessage());
        }
    }

    private void cargarEstudiantes(Sistema sistema) {
        String sql = "SELECT u.cedula, u.nombre, u.apellido, u.correo, u.contrasena, e.semestre " +
                     "FROM usuarios u JOIN estudiantes e ON u.cedula = e.cedula";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante nuevo = new Estudiante(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getInt("semestre")
                );
                sistema.registrarEstudiante(nuevo);
            }
        } catch (Exception e) {
            System.out.println("Error cargando estudiantes: " + e.getMessage());
        }
    }

    private void cargarMaterias(Sistema sistema) {
        String sql = "SELECT codigo, nombre, creditos, cedula_docente FROM materias";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Docente doc = sistema.buscarDocente(rs.getString("cedula_docente"));
                if (doc != null) {
                    materia nueva = new materia(
                        rs.getString("codigo"),
                        rs.getInt("creditos"),
                        rs.getString("nombre"),
                        doc
                    );
                    sistema.registrarMateria(nueva);
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando materias: " + e.getMessage());
        }
    }

    private void cargarActividades(Sistema sistema) {
        String sql = "SELECT id, nombre, porcentaje, codigo_materia FROM actividades";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                materia m = sistema.buscarMateria(rs.getString("codigo_materia"));
                if (m != null) {
                    m.getActividades().add(new actividad(
                        rs.getString("nombre"),
                        rs.getDouble("porcentaje")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando actividades: " + e.getMessage());
        }
    }

    private void cargarMatriculas(Sistema sistema) {
        String sql = "SELECT cedula_estudiante, codigo_materia FROM matriculas";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante est = sistema.buscarEstudiante(rs.getString("cedula_estudiante"));
                materia m      = sistema.buscarMateria(rs.getString("codigo_materia"));
                if (est != null && m != null)
                    est.matricular(m);
            }
        } catch (Exception e) {
            System.out.println("Error cargando matrículas: " + e.getMessage());
        }
    }

    private void cargarCalificaciones(Sistema sistema) {
        String sql = "SELECT c.cedula_estudiante, c.id_actividad, c.valor, a.codigo_materia, a.nombre " +
                     "FROM calificaciones c JOIN actividades a ON c.id_actividad = a.id";
        try (Connection conn = ConexionDB.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante est = sistema.buscarEstudiante(rs.getString("cedula_estudiante"));
                materia m      = sistema.buscarMateria(rs.getString("codigo_materia"));
                if (est != null && m != null) {
                    String nombreAct = rs.getString("nombre");
                    for (actividad a : m.getActividades()) {
                        if (a.getNombre().equals(nombreAct)) {
                            m.getCalificaciones().add(new calificacion(est, a, rs.getDouble("valor")));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando calificaciones: " + e.getMessage());
        }
    }

    // ── Guardar ──────────────────────────────────────────────────────────────

    public void guardarAdministrador(Administrador a) {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO usuarios (cedula, nombre, apellido, correo, contrasena, rol) " +
                "VALUES (?, ?, ?, ?, ?, 'Administrador') ON CONFLICT (cedula) DO NOTHING");
            ps1.setString(1, a.getCedula());
            ps1.setString(2, a.getNombre());
            ps1.setString(3, a.getApellido());
            ps1.setString(4, a.getCorreo());
            ps1.setString(5, a.getContrasena());
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO administradores (cedula) VALUES (?) ON CONFLICT DO NOTHING");
            ps2.setString(1, a.getCedula());
            ps2.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando administrador: " + e.getMessage());
        }
    }

    public void guardarDocente(Docente d) {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO usuarios (cedula, nombre, apellido, correo, contrasena, rol) " +
                "VALUES (?, ?, ?, ?, ?, 'Docente') ON CONFLICT (cedula) DO NOTHING");
            ps1.setString(1, d.getCedula());
            ps1.setString(2, d.getNombre());
            ps1.setString(3, d.getApellido());
            ps1.setString(4, d.getCorreo());
            ps1.setString(5, d.getContrasena());
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO docentes (cedula, especialidad) VALUES (?, ?) ON CONFLICT DO NOTHING");
            ps2.setString(1, d.getCedula());
            ps2.setString(2, d.getEspecialidad());
            ps2.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando docente: " + e.getMessage());
        }
    }

    public void guardarEstudiante(Estudiante e) {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO usuarios (cedula, nombre, apellido, correo, contrasena, rol) " +
                "VALUES (?, ?, ?, ?, ?, 'Estudiante') ON CONFLICT (cedula) DO NOTHING");
            ps1.setString(1, e.getCedula());
            ps1.setString(2, e.getNombre());
            ps1.setString(3, e.getApellido());
            ps1.setString(4, e.getCorreo());
            ps1.setString(5, e.getContrasena());
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO estudiantes (cedula, semestre) VALUES (?, ?) ON CONFLICT DO NOTHING");
            ps2.setString(1, e.getCedula());
            ps2.setInt(2, e.getSemestre());
            ps2.executeUpdate();
        } catch (Exception ex) {
            System.out.println("Error guardando estudiante: " + ex.getMessage());
        }
    }

    public void guardarMateria(materia m) {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO materias (codigo, nombre, creditos, cedula_docente) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING")) {
            ps.setString(1, m.getCodigoMateria());
            ps.setString(2, m.getNombre());
            ps.setInt(3, m.getCreditos());
            ps.setString(4, m.getDocente().getCedula());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando materia: " + e.getMessage());
        }
    }

    public void guardarActividad(actividad a, String codigoMateria) {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO actividades (nombre, porcentaje, codigo_materia) VALUES (?, ?, ?)")) {
            ps.setString(1, a.getNombre());
            ps.setDouble(2, a.getPorcentaje());
            ps.setString(3, codigoMateria);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando actividad: " + e.getMessage());
        }
    }

    public void guardarMatricula(String cedulaEstudiante, String codigoMateria) {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO matriculas (cedula_estudiante, codigo_materia) VALUES (?, ?) ON CONFLICT DO NOTHING")) {
            ps.setString(1, cedulaEstudiante);
            ps.setString(2, codigoMateria);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando matrícula: " + e.getMessage());
        }
    }

    public void guardarCalificacion(calificacion c, String codigoMateria) {
        try (Connection conn = ConexionDB.conectar()) {
            PreparedStatement buscar = conn.prepareStatement(
                "SELECT id FROM actividades WHERE nombre = ? AND codigo_materia = ?");
            buscar.setString(1, c.getActividad().getNombre());
            buscar.setString(2, codigoMateria);
            ResultSet rs = buscar.executeQuery();
            if (rs.next()) {
                int idActividad = rs.getInt("id");
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO calificaciones (cedula_estudiante, id_actividad, valor) " +
                    "VALUES (?, ?, ?) ON CONFLICT (cedula_estudiante, id_actividad) DO UPDATE SET valor = ?");
                ps.setString(1, c.getEstudiante().getCedula());
                ps.setInt(2, idActividad);
                ps.setDouble(3, c.getValor());
                ps.setDouble(4, c.getValor());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error guardando calificación: " + e.getMessage());
        }
    }

    // ── Eliminar ─────────────────────────────────────────────────────────────

    public void eliminarUsuario(String cedula) {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM usuarios WHERE cedula = ?")) {
            ps.setString(1, cedula);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error eliminando usuario: " + e.getMessage());
        }
    }

    public void eliminarMateria(String codigo) {
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM materias WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error eliminando materia: " + e.getMessage());
        }
    }
}
