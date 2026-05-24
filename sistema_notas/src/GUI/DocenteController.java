package gui;

import Sistema.Sistema;
import User.user;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
import Materia.actividad;
import Materia.calificacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

public class DocenteController implements ControladorBase {

    @FXML private Label labelBienvenida;

    // Combo materias
    @FXML private ComboBox<String> comboMaterias;

    // Tabla calificaciones
    @FXML private TableView<calificacion> tablaCalificaciones;
    @FXML private TableColumn<calificacion, String> colCalEstudiante;
    @FXML private TableColumn<calificacion, String> colCalActividad;
    @FXML private TableColumn<calificacion, String> colCalPorcentaje;
    @FXML private TableColumn<calificacion, String> colCalNota;

    // Formulario actividad
    @FXML private TextField campoActNombre;
    @FXML private TextField campoActPorcentaje;
    @FXML private Label     labelActMsg;

    // Formulario calificacion
    @FXML private TextField campoCalCedulaEst;
    @FXML private ComboBox<String> comboActividades;
    @FXML private TextField campoCalNota;
    @FXML private Label     labelCalMsg;

    // Nota final
    @FXML private TextField campoNotaFinalCedula;
    @FXML private Label     labelNotaFinal;

    private Sistema sistema;
    private Docente docente;

    @Override
    public void inicializar(Sistema sistema, user usuario) {
        this.sistema  = sistema;
        this.docente  = (Docente) usuario;
        labelBienvenida.setText("Bienvenido, " + docente.getNombre() + " " + docente.getApellido());

        configurarTabla();
        cargarComboMaterias();
    }

    private void configurarTabla() {
        colCalEstudiante.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getEstudiante().getNombre() + " " + d.getValue().getEstudiante().getApellido()));
        colCalActividad.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getActividad().getNombre()));
        colCalPorcentaje.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getActividad().getPorcentaje() + "%"));
        colCalNota.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(d.getValue().getValor())));
    }

    private void cargarComboMaterias() {
        comboMaterias.getItems().clear();
        for (materia m : docente.getMaterias())
            comboMaterias.getItems().add(m.getCodigoMateria() + " - " + m.getNombre());
    }

    private materia getMateriaSeleccionada() {
        int idx = comboMaterias.getSelectionModel().getSelectedIndex();
        if (idx < 0) return null;
        return docente.getMaterias().get(idx);
    }

    @FXML
    private void onCerrarSesion(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/login.fxml"));
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
    
    @FXML
    private void onSeleccionarMateria() {
        materia m = getMateriaSeleccionada();
        if (m == null) return;
        tablaCalificaciones.setItems(FXCollections.observableArrayList(m.getCalificaciones()));
        comboActividades.getItems().clear();
        for (actividad a : m.getActividades())
            comboActividades.getItems().add(a.getNombre() + " (" + a.getPorcentaje() + "%)");
    }

    @FXML
    private void onAgregarActividad() {
        materia m = getMateriaSeleccionada();
        if (m == null) { labelActMsg.setText("Selecciona una materia primero."); return; }
        try {
            String nom = campoActNombre.getText().trim();
            double por = Double.parseDouble(campoActPorcentaje.getText().trim());

            double acumulado = m.getActividades().stream().mapToDouble(actividad::getPorcentaje).sum();
            if (acumulado + por > 100) {
                labelActMsg.setStyle("-fx-text-fill: #e94560;");
                labelActMsg.setText("Supera el 100%. Acumulado: " + acumulado + "%");
                return;
            }
            m.getActividades().add(new actividad(nom, por));
            labelActMsg.setStyle("-fx-text-fill: #00ff88;");
            labelActMsg.setText("Actividad agregada. Total: " + (acumulado + por) + "%");
            campoActNombre.clear(); campoActPorcentaje.clear();
            onSeleccionarMateria();
        } catch (NumberFormatException ex) {
            labelActMsg.setStyle("-fx-text-fill: #e94560;");
            labelActMsg.setText("El porcentaje debe ser un número.");
        }
    }

    @FXML
    private void onRegistrarCalificacion() {
        materia m = getMateriaSeleccionada();
        if (m == null) { labelCalMsg.setText("Selecciona una materia primero."); return; }

        int idxAct = comboActividades.getSelectionModel().getSelectedIndex();
        if (idxAct < 0) { labelCalMsg.setText("Selecciona una actividad."); return; }

        try {
            String cedula = campoCalCedulaEst.getText().trim();
            double nota   = Double.parseDouble(campoCalNota.getText().trim());

            Estudiante est = sistema.buscarEstudiante(cedula);
            if (est == null) {
                labelCalMsg.setStyle("-fx-text-fill: #e94560;");
                labelCalMsg.setText("Estudiante no encontrado.");
                return;
            }

            actividad act = m.getActividades().get(idxAct);
            boolean ok = sistema.registrarCalificacion(m, est, act, nota);
            if (ok) {
                labelCalMsg.setStyle("-fx-text-fill: #00ff88;");
                labelCalMsg.setText("Calificación registrada.");
                campoCalCedulaEst.clear(); campoCalNota.clear();
                onSeleccionarMateria();
            } else {
                labelCalMsg.setStyle("-fx-text-fill: #e94560;");
                labelCalMsg.setText("Error: nota inválida o estudiante no matriculado.");
            }
        } catch (NumberFormatException ex) {
            labelCalMsg.setStyle("-fx-text-fill: #e94560;");
            labelCalMsg.setText("La nota debe ser un número entre 0 y 5.");
        }
    }

    @FXML
    private void onVerNotaFinal() {
        materia m = getMateriaSeleccionada();
        if (m == null) { labelNotaFinal.setText("Selecciona una materia primero."); return; }

        String cedula = campoNotaFinalCedula.getText().trim();
        Estudiante est = sistema.buscarEstudiante(cedula);
        if (est == null) {
            labelNotaFinal.setStyle("-fx-text-fill: #e94560;");
            labelNotaFinal.setText("Estudiante no encontrado.");
            return;
        }
        double nota = sistema.calcularNotaFinal(m, est);
        labelNotaFinal.setStyle("-fx-text-fill: #00ff88;");
        labelNotaFinal.setText("Nota final de " + est.getNombre() + ": " + nota);
    }
}
