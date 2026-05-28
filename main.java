import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Sistema.Sistema;
import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
import gui.LoginController;

public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Sistema sistema = new Sistema();

        // ── Admin ─────────────────────────────────────────────────────────────
        sistema.registrarAdministrador("000", "Admin", "Principal", "admin@test.com", "1234");
        Administrador admin = (Administrador) sistema.login("admin@test.com", "1234");

        // ── Docentes ──────────────────────────────────────────────────────────
        Docente doc1 = admin.crear_docente("111", "Carlos", "Pérez",  "carlos@test.com", "1234", "111", "Matemáticas");
        Docente doc2 = admin.crear_docente("222", "Laura",  "Gómez",  "laura@test.com",  "1234", "222", "Programación");
        sistema.registrarDocente(doc1);
        sistema.registrarDocente(doc2);

        // ── Materias ──────────────────────────────────────────────────────────
        materia mat1 = admin.crear_materia("MAT101", 3, "Cálculo",         doc1);
        materia mat2 = admin.crear_materia("PRG101", 4, "Programación I",  doc2);
        sistema.registrarMateria(mat1);
        sistema.registrarMateria(mat2);

        // ── Estudiantes ───────────────────────────────────────────────────────
        Estudiante est1 = admin.crear_estudiante("333", "Juan",  "Torres", "juan@test.com",  "1234", "333", 1);
        Estudiante est2 = admin.crear_estudiante("444", "María", "López",  "maria@test.com", "1234", "444", 2);
        sistema.registrarEstudiante(est1);
        sistema.registrarEstudiante(est2);

        // ── Matrículas ────────────────────────────────────────────────────────
        est1.matricular(mat1);
        est1.matricular(mat2);
        est2.matricular(mat1);

        // ── Actividades ───────────────────────────────────────────────────────
        doc1.crear_actividad("Parcial 1", 30, mat1);
        doc1.crear_actividad("Parcial 2", 30, mat1);
        doc1.crear_actividad("Final",     40, mat1);

        doc2.crear_actividad("Taller 1", 50, mat2);
        doc2.crear_actividad("Proyecto", 50, mat2);

        // ── Calificaciones ────────────────────────────────────────────────────
        sistema.registrarCalificacion(mat1, est1, mat1.getActividades().get(0), 4.5);
        sistema.registrarCalificacion(mat1, est1, mat1.getActividades().get(1), 3.8);
        sistema.registrarCalificacion(mat1, est1, mat1.getActividades().get(2), 4.2);
        sistema.registrarCalificacion(mat1, est2, mat1.getActividades().get(0), 4.0);
        sistema.registrarCalificacion(mat1, est2, mat1.getActividades().get(1), 3.5);
        sistema.registrarCalificacion(mat2, est1, mat2.getActividades().get(0), 5.0);

        // ── Cargar pantalla de login ──────────────────────────────────────────
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        LoginController ctrl = loader.getController();
        ctrl.setSistema(sistema);

        stage.setTitle("Sistema de Notas");
        stage.setScene(new Scene(root, 420, 500));
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
