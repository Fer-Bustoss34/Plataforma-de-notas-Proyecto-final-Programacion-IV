package gui;

import Sistema.Sistema;
import User.user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML private TextField     campoCorreo;
    @FXML private PasswordField campoContrasena;
    @FXML private Label         mensajeError;

    private Sistema sistema;

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    @FXML
    private void onLogin(ActionEvent e) {
        String correo     = campoCorreo.getText().trim();
        String contrasena = campoContrasena.getText().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mensajeError.setText("Por favor completa todos los campos.");
            return;
        }

        user usuario = sistema.login(correo, contrasena);

        if (usuario == null) {
            mensajeError.setText("Correo o contraseña incorrectos.");
            return;
        }

        // Abre la pantalla según el rol
        try {
            String fxml;
            switch (usuario.getRol()) {
                case "Administrador" -> fxml = "/resources/admin.fxml";
                case "Docente"       -> fxml = "/resources/docente.fxml";
                case "Estudiante"    -> fxml = "/resources/estudiante.fxml";
                default -> { mensajeError.setText("Rol desconocido."); return; }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Pasa el sistema y el usuario al siguiente controlador
            ControladorBase ctrl = loader.getController();
            ctrl.inicializar(sistema, usuario);

            Stage stage = (Stage) campoCorreo.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 650));
            stage.centerOnScreen();

        } catch (Exception ex) {
            mensajeError.setText("Error al cargar la pantalla.");
            ex.printStackTrace();
        }
    }
}