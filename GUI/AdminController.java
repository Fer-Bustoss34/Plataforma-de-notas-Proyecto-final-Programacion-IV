package gui;

import Sistema.Sistema;
import User.user;
import Administrador.Administrador;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class AdminController implements ControladorBase {

    @FXML private Label labelBienvenida;

    // Tablas
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, String> colEstCedula;
    @FXML private TableColumn<Estudiante, String> colEstNombre;
    @FXML private TableColumn<Estudiante, String> colEstCorreo;
    @FXML private TableColumn<Estudiante, String> colEstSemestre;

    @FXML private TableView<Docente> tablaDocentes;
    @FXML private TableColumn<Docente, String> colDocCedula;
    @FXML private TableColumn<Docente, String> colDocNombre;
    @FXML private TableColumn<Docente, String> colDocCorreo;
    @FXML private TableColumn<Docente, String> colDocEspecialidad;

    @FXML private TableView<materia> tablaMaterias;
    @FXML private TableColumn<materia, String> colMatCodigo;
    @FXML private TableColumn<materia, String> colMatNombre;
    @FXML private TableColumn<materia, String> colMatCreditos;
    @FXML private TableColumn<materia, String> colMatDocente;

    // Formulario estudiante
    @FXML private TextField campoEstCedula;
    @FXML private TextField campoEstNombre;
    @FXML private TextField campoEstApellido;
    @FXML private TextField campoEstCorreo;
    @FXML private TextField campoEstContrasena;
    @FXML private TextField campoEstCodigo;
    @FXML private TextField campoEstSemestre;
    @FXML private Label     labelEstMsg;

    // Formulario docente
    @FXML private TextField campoDocCedula;
    @FXML private TextField campoDocNombre;
    @FXML private TextField campoDocApellido;
    @FXML private TextField campoDocCorreo;
    @FXML private TextField campoDocContrasena;
    @FXML private TextField campoDocCodigo;
    @FXML private TextField campoDocEspecialidad;
    @FXML private Label     labelDocMsg;

    // Formulario materia
    @FXML private TextField campoMatCodigo;
    @FXML private TextField campoMatNombre;
    @FXML private TextField campoMatCreditos;
    @FXML private TextField campoMatCedulaDocente;
    @FXML private Label     labelMatMsg;

    private Sistema      sistema;
    private Administrador admin;

    @Override
    public void inicializar(Sistema sistema, user usuario) {
        this.sistema = sistema;
        this.admin   = (Administrador) usuario;
        labelBienvenida.setText("Bienvenido, " + admin.getNombre() + " " + admin.getApellido());
        configurarTablas();
        cargarDatos();
    }

    private void configurarTablas() {
        colEstCedula.setCellValueFactory(d   -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCedula()));
        colEstNombre.setCellValueFactory(d   -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre() + " " + d.getValue().getApellido()));
        colEstCorreo.setCellValueFactory(d   -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCorreo()));
        colEstSemestre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getSemestre())));

        colDocCedula.setCellValueFactory(d        -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCedula()));
        colDocNombre.setCellValueFactory(d        -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre() + " " + d.getValue().getApellido()));
        colDocCorreo.setCellValueFactory(d        -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCorreo()));
        colDocEspecialidad.setCellValueFactory(d  -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEspecialidad()));

        colMatCodigo.setCellValueFactory(d   -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCodigoMateria()));
        colMatNombre.setCellValueFactory(d   -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colMatCreditos.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getCreditos())));
        colMatDocente.setCellValueFactory(d  -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDocente().getNombre() + " " + d.getValue().getDocente().getApellido()));
    }

    private void cargarDatos() {
        tablaEstudiantes.setItems(FXCollections.observableArrayList(sistema.getEstudiantes()));
        tablaDocentes.setItems(FXCollections.observableArrayList(sistema.getDocentes()));
        tablaMaterias.setItems(FXCollections.observableArrayList(sistema.getMaterias()));
    }

    @FXML
    private void onRegistrarEstudiante() {
        try {
            String ced = campoEstCedula.getText().trim();
            String nom = campoEstNombre.getText().trim();
            String ape = campoEstApellido.getText().trim();
            String cor = campoEstCorreo.getText().trim();
            String con = campoEstContrasena.getText().trim();
            String cod = campoEstCodigo.getText().trim();
            int    sem = Integer.parseInt(campoEstSemestre.getText().trim());

            // Usa el método de Administrador para crear el estudiante
            Estudiante nuevo = admin.crear_estudiante(ced, nom, ape, cor, con, cod, sem);
            boolean ok = sistema.registrarEstudiante(nuevo);

            if (ok) {
                labelEstMsg.setStyle("-fx-text-fill: #00ff88;");
                labelEstMsg.setText("Estudiante registrado correctamente.");
                cargarDatos();
                campoEstCedula.clear(); campoEstNombre.clear(); campoEstApellido.clear();
                campoEstCorreo.clear(); campoEstContrasena.clear();
                campoEstCodigo.clear(); campoEstSemestre.clear();
            } else {
                labelEstMsg.setStyle("-fx-text-fill: #e94560;");
                labelEstMsg.setText("El correo ya está registrado.");
            }
        } catch (NumberFormatException ex) {
            labelEstMsg.setStyle("-fx-text-fill: #e94560;");
            labelEstMsg.setText("El semestre debe ser un número.");
        }
    }

    @FXML
    private void onEliminarEstudiante() {
        Estudiante sel = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (sel == null) { labelEstMsg.setText("Selecciona un estudiante primero."); return; }
        sistema.eliminarEstudiante(sel.getCedula());
        cargarDatos();
        labelEstMsg.setStyle("-fx-text-fill: #00ff88;");
        labelEstMsg.setText("Estudiante eliminado.");
    }

    @FXML
    private void onRegistrarDocente() {
        String ced = campoDocCedula.getText().trim();
        String nom = campoDocNombre.getText().trim();
        String ape = campoDocApellido.getText().trim();
        String cor = campoDocCorreo.getText().trim();
        String con = campoDocContrasena.getText().trim();
        String cod = campoDocCodigo.getText().trim();
        String esp = campoDocEspecialidad.getText().trim();

        // Usa el método de Administrador para crear el docente
        Docente nuevo = admin.crear_docente(ced, nom, ape, cor, con, cod, esp);
        boolean ok = sistema.registrarDocente(nuevo);

        if (ok) {
            labelDocMsg.setStyle("-fx-text-fill: #00ff88;");
            labelDocMsg.setText("Docente registrado correctamente.");
            cargarDatos();
            campoDocCedula.clear(); campoDocNombre.clear(); campoDocApellido.clear();
            campoDocCorreo.clear(); campoDocContrasena.clear();
            campoDocCodigo.clear(); campoDocEspecialidad.clear();
        } else {
            labelDocMsg.setStyle("-fx-text-fill: #e94560;");
            labelDocMsg.setText("El correo ya está registrado.");
        }
    }

    @FXML
    private void onEliminarDocente() {
        Docente sel = tablaDocentes.getSelectionModel().getSelectedItem();
        if (sel == null) { labelDocMsg.setText("Selecciona un docente primero."); return; }
        sistema.eliminarDocente(sel.getCedula());
        cargarDatos();
        labelDocMsg.setStyle("-fx-text-fill: #00ff88;");
        labelDocMsg.setText("Docente eliminado.");
    }

    @FXML
    private void onRegistrarMateria() {
        try {
            String cod    = campoMatCodigo.getText().trim();
            String nom    = campoMatNombre.getText().trim();
            int    cred   = Integer.parseInt(campoMatCreditos.getText().trim());
            String cedDoc = campoMatCedulaDocente.getText().trim();

            Docente doc = sistema.buscarDocente(cedDoc);
            if (doc == null) {
                labelMatMsg.setStyle("-fx-text-fill: #e94560;");
                labelMatMsg.setText("Docente no encontrado.");
                return;
            }

            // Usa el método de Administrador para crear la materia
            materia nueva = admin.crear_materia(cod, cred, nom, doc);
            boolean ok = sistema.registrarMateria(nueva);

            if (ok) {
                labelMatMsg.setStyle("-fx-text-fill: #00ff88;");
                labelMatMsg.setText("Materia registrada correctamente.");
                cargarDatos();
                campoMatCodigo.clear(); campoMatNombre.clear();
                campoMatCreditos.clear(); campoMatCedulaDocente.clear();
            } else {
                labelMatMsg.setStyle("-fx-text-fill: #e94560;");
                labelMatMsg.setText("El código de materia ya existe.");
            }
        } catch (NumberFormatException ex) {
            labelMatMsg.setStyle("-fx-text-fill: #e94560;");
            labelMatMsg.setText("Los créditos deben ser un número.");
        }
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
}
