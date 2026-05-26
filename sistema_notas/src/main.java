import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Sistema.Sistema;
import gui.LoginController;

public class main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Sistema sistema = new Sistema();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/login.fxml"));
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