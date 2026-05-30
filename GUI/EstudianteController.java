package gui;

import Sistema.Sistema;
import User.user;
import Estudiante.Estudiante;
import Materia.materia;
import Materia.calificacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class EstudianteController implements ControladorBase {

    @FXML private Label labelBienvenida;

    @FXML private TableView<materia> tablaMisMaterias;
    @FXML private TableColumn<materia, String> colMatCodigo;
    @FXML private TableColumn<materia, String> colMatNombre;
    @FXML private TableColumn<materia, String> colMatDocente;
    @FXML private TableColumn<materia, String> colMatNota;

    @FXML private TableView<calificacion> tablaCalificaciones;
    @FXML private TableColumn<calificacion, String> colCalMateria;
    @FXML private TableColumn<calificacion, String> colCalActividad;
    @FXML private TableColumn<calificacion, String> colCalPorcentaje;
    @FXML private TableColumn<calificacion, String> colCalNota;

    @FXML private ComboBox<String> comboMateriasDisponibles;
    @FXML private Label labelMatriculaMsg;

    private Sistema    sistema;
    private Estudiante estudiante;

    @Override
    public void inicializar(Sistema sistema, user usuario) {
        this.sistema    = sistema;
        this.estudiante = (Estudiante) usuario;
        labelBienvenida.setText("Bienvenido, " + estudiante.getNombre() + " " + estudiante.getApellido());
        configurarTablas();
        cargarDatos();
    }

    private void configurarTablas() {
        colMatCodigo.setCellValueFactory(d  -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCodigoMateria()));
        colMatNombre.setCellValueFactory(d  -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colMatDocente.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getDocente().getNombre() + " " + d.getValue().getDocente().getApellido()));
        colMatNota.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(sistema.calcularNotaFinal(d.getValue(), estudiante))));

        colCalMateria.setCellValueFactory(d -> {
            for (materia m : estudiante.getMaterias())
                for (calificacion c : m.getCalificaciones())
                    if (c == d.getValue())
                        return new javafx.beans.property.SimpleStringProperty(m.getNombre());
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        colCalActividad.setCellValueFactory(d  -> new javafx.beans.property.SimpleStringProperty(d.getValue().getActividad().getNombre()));
        colCalPorcentaje.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getActividad().getPorcentaje() + "%"));
        colCalNota.setCellValueFactory(d       -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getValor())));
    }

    private void cargarDatos() {
        tablaMisMaterias.setItems(FXCollections.observableArrayList(estudiante.getMaterias()));

        java.util.ArrayList<calificacion> misNotas = new java.util.ArrayList<>();
        for (materia m : estudiante.getMaterias())
            for (calificacion c : m.getCalificaciones())
                if (c.getEstudiante().getCedula().equals(estudiante.getCedula()))
                    misNotas.add(c);
        tablaCalificaciones.setItems(FXCollections.observableArrayList(misNotas));

        comboMateriasDisponibles.getItems().clear();
        for (materia m : sistema.getMaterias())
            if (!estudiante.getMaterias().contains(m))
                comboMateriasDisponibles.getItems().add(m.getCodigoMateria() + " - " + m.getNombre());
    }

    @FXML
    private void onMatricular() {
        int idx = comboMateriasDisponibles.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            labelMatriculaMsg.setStyle("-fx-text-fill: #e94560;");
            labelMatriculaMsg.setText("Selecciona una materia.");
            return;
        }

        // Buscar la materia disponible por índice
        int contador = 0;
        for (materia m : sistema.getMaterias()) {
            if (!estudiante.getMaterias().contains(m)) {
                if (contador == idx) {
                    // Usa el método matricular() de Estudiante
                    String resultado = sistema.matricularEstudiante(estudiante, m);
                    if (resultado.startsWith("Matrícula exitosa")) {
                        labelMatriculaMsg.setStyle("-fx-text-fill: #00ff88;");
                        cargarDatos();
                    } else {
                        labelMatriculaMsg.setStyle("-fx-text-fill: #e94560;");
                    }
                    labelMatriculaMsg.setText(resultado);
                    return;
                }
                contador++;
            }
        }
    }

    @FXML
    private void onCerrarSesion(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setSistema(sistema);
            Stage stage = (Stage) labelBienvenida.getScene().getWindow();
            stage.setScene(new Scene(root, 420, 500));
            stage.centerOnScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
