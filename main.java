import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Sistema.Sistema;
import gui.LoginController;
import DB.Repositorio;

public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ── Inicializar sistema y cargar datos desde Supabase ─────────────────
        Sistema sistema = new Sistema();
        Repositorio repo = new Repositorio();
        repo.cargarTodo(sistema);

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