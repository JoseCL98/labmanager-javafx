
package com.myorg.labmanager.ui;

import com.myorg.labmanager.config.Container;
import com.myorg.labmanager.domain.model.Equipo;
import com.myorg.labmanager.domain.model.Prestamo;
import com.myorg.labmanager.domain.model.Usuario;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainController {
    private final Container container;
    private final ObservableList<Equipo> equipos = FXCollections.observableArrayList();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private final ObservableList<Prestamo> prestamos = FXCollections.observableArrayList();

    public MainController(Container container) {
        this.container = container;
    }

    // Tabla de equipos
    @FXML private TableView<Equipo> tableEquipos;
    @FXML private TableColumn<Equipo, Integer> colId;
    @FXML private TableColumn<Equipo, String> colNombre;
    @FXML private TableColumn<Equipo, String> colTipo;
    @FXML private TableColumn<Equipo, Integer> colDisponibilidad;
    @FXML private TableColumn<Equipo, Integer> colTotal;
    
    // Tabla de usuario
    @FXML private TableView<Usuario> tableUsuarios;
    @FXML private TableColumn<Usuario, Integer> colUsuarioId;
    @FXML private TableColumn<Usuario, String> colUsuarioNombre;
    @FXML private TableColumn<Usuario, String> colUsuarioTipo;
    
    // Tabla de préstamos
    @FXML private TableView<Prestamo> tablePrestamos;
    @FXML private TableColumn<Prestamo, Integer> colPrestamoId;
    @FXML private TableColumn<Prestamo, Integer> colEquipoId;
    @FXML private TableColumn<Prestamo, String> colEstado;
    @FXML private TableColumn<Prestamo, Integer> colCantidad;
    @FXML private TableColumn<Prestamo, LocalDateTime> colFecha;
    @FXML private TableColumn<Prestamo, LocalDateTime> colFechaD;
    
    // Campos de entrada
    @FXML private ComboBox<Usuario> comboUsuarios;
    @FXML private ComboBox<Equipo> comboEquipos;
    @FXML private TextField inputCantidadPrestamo;
    
    // Área de reportes
    @FXML private TextArea areaReporte;

    @FXML
    public void initialize() {
        // Configurar tabla de equipos
        colId.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getId()));
        colNombre.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getNombre()));
        colTipo.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getTipo()));
        colDisponibilidad.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getCantidadDisponible()));
        colTotal.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getCantidadTotal()));
        
        // Configurar tabla de usuarios
        colUsuarioId.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty(cd.getValue().getId()));
        colUsuarioNombre.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getNombre()));
        colUsuarioTipo.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getTipo()));
        
        // Configurar tabla de préstamos
        colPrestamoId.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getId()));
        colEquipoId.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getEquipoId()));
        colEstado.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getEstado()));
        colCantidad.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty(cd.getValue().getCantidad()));
        colFecha.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty(cd.getValue().getFechaPrestamo()));
        colFechaD.setCellValueFactory(cd -> 
            new javafx.beans.property.SimpleObjectProperty(cd.getValue().getFechaDevolucion()));
        
        // Cargar datos
        refrescarEquipos();
        refrescarUsuarios();
        refrescarPrestamos();
        //cargarUsuarios();
        
        tableEquipos.setItems(equipos);
        tableUsuarios.setItems(usuarios);
        tablePrestamos.setItems(prestamos);
    }

    @FXML
    public void onAgregarEquipo() {
        // Mostrar diálogo con campos precargados
        TextField nombreField = new TextField();
        TextField tipoField = new TextField();
        TextField totalField = new TextField();

        VBox vbox = new VBox(5, new Label("Nombre:"), nombreField,
                             new Label("Tipo:"), tipoField,
                             new Label("Total:"), totalField);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Agregar equipo");
        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                container.getEquipoService().create(tipoField.getText(), nombreField.getText(), Integer.parseInt(totalField.getText()));
                showAlert("Éxito", "Equipo agregado correctamente", Alert.AlertType.INFORMATION);
                refrescarEquipos();
            } catch (Exception ex) {
                showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    public void onEditarEquipo() {
        Equipo e = tableEquipos.getSelectionModel().getSelectedItem();
        if (e == null) {showAlert("Error", "Seleccione un equipo", Alert.AlertType.WARNING); return;}

        // Mostrar diálogo con campos precargados
        TextField nombreField = new TextField(e.getNombre());
        TextField tipoField = new TextField(e.getTipo());
        TextField totalField = new TextField(String.valueOf(e.getCantidadTotal()));

        VBox vbox = new VBox(5, new Label("Nombre:"), nombreField,
                             new Label("Tipo:"), tipoField,
                             new Label("Total:"), totalField);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Editar equipo");
        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                container.getEquipoService().update(
                    e.getId(),
                    nombreField.getText(),
                    tipoField.getText(),
                    Integer.parseInt(totalField.getText())
                );
                showAlert("Éxito", "Equipo actualizado correctamente", Alert.AlertType.INFORMATION);
                refrescarEquipos();
            } catch (Exception ex) {
                showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    public void onEliminarEquipo() {
        Equipo e = tableEquipos.getSelectionModel().getSelectedItem();
        if (e == null) {showAlert("Error", "Seleccione un equipo", Alert.AlertType.WARNING); return;}
        
        boolean exito = container.getEquipoService().delete(e.getId());
        if(exito){
            equipos.remove(e);
            showAlert("Éxito", "Equipo eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No se puede eliminar tiene prestamos activos", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onPrestar() {
        
        List<Usuario> listaUsuarios = container.getUsuarioService().listAll();
        List<Equipo> listaEquipos = container.getEquipoService().listAll();
        
        // Mostrar diálogo        
        ChoiceBox<Usuario> usuarios = new ChoiceBox<>();
        usuarios.getItems().addAll(listaUsuarios);
        
        ChoiceBox<Equipo> equipos = new ChoiceBox<>();
        equipos.getItems().addAll(listaEquipos);
        
        TextField cantidad = new TextField();

        VBox vbox = new VBox(5, new Label("Usuario:"), usuarios,
                             new Label("Equipo:"), equipos,  
                             new Label("Cantidad:"), cantidad);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Prestar");
        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                if (usuarios.getValue() == null) {
                    showAlert("Error", "Seleccione un usuario", Alert.AlertType.WARNING);
                    return;
                }
                if (equipos.getValue() == null) {
                    showAlert("Error", "Seleccione un equipo", Alert.AlertType.WARNING);
                    return;
                }     
                if (cantidad.getText().isBlank()) {
                    showAlert("Error", "Cantidad inválida", Alert.AlertType.ERROR);
                    return;
                }
                
                
                boolean exito = container.getPrestamoService().prestar(
                    equipos.getValue().getId(), 
                    usuarios.getValue(), 
                    Integer.parseInt(cantidad.getText())
                );
                if (exito) {
                    refrescarEquipos();
                    refrescarPrestamos();
                    showAlert("Éxito", "Préstamo registrado correctamente", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "No se pudo realizar el préstamo. Verifique disponibilidad y límites.", 
                        Alert.AlertType.ERROR);
                }
            } catch (Exception ex) {
                showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
            }
        } 
    }

    @FXML
    public void onDevolver() {
        Prestamo selectedPrestamo = tablePrestamos.getSelectionModel().getSelectedItem();
        
        if (selectedPrestamo == null) {
            showAlert("Error", "Seleccione un préstamo", Alert.AlertType.WARNING);
            return;
        }
        
        if (!selectedPrestamo.isPrestado()) {
            showAlert("Error", "Este préstamo ya fue devuelto", Alert.AlertType.WARNING);
            return;
        }
        
        boolean exito = container.getPrestamoService().devolver(selectedPrestamo.getId());
        
        if (exito) {
            refrescarEquipos();
            refrescarPrestamos();
            showAlert("Éxito", "Devolución registrada correctamente", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No se pudo procesar la devolución", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onGenerarReporte() {
        String reporte = container.getReporteFacade().generarReporteTexto();
        String estadisticas = container.getReporteFacade().generarEstadisticasPorTipo();
        areaReporte.setText(reporte + "\n" + estadisticas);
    }

    @FXML
    public void onImportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Equipos desde CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );
        
        File file = fileChooser.showOpenDialog(tableEquipos.getScene().getWindow());
        
        if (file != null) {
            try {
                List<Equipo> equiposImportados = container.getCsvImporter()
                    .importarEquipos(file.getAbsolutePath());
                
                // Guardar en la base de datos
                for (Equipo equipo : equiposImportados) {
                    container.getEquipoService().create(
                        equipo.getTipo(), 
                        equipo.getNombre(), 
                        equipo.getCantidadTotal()
                    );
                }
                
                refrescarEquipos();
                showAlert("Éxito", 
                    "Se importaron " + equiposImportados.size() + " equipos correctamente", 
                    Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                showAlert("Error", 
                    "Error al importar CSV: " + e.getMessage(), 
                    Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void onAgregarUsuario() {
        // Mostrar diálogo
        TextField nombreF = new TextField();
        ChoiceBox<String> tipoD = new ChoiceBox<>();
        tipoD.getItems().addAll("estudiante", "profesor", "administrador");

        VBox vbox = new VBox(5, new Label("Nombre:"), nombreF,
                             new Label("Tipo:"), tipoD);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Agregar usuario");
        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                container.getUsuarioService().create(
                    nombreF.getText(),
                    tipoD.getValue());
                showAlert("Éxito", "Usuario agregado correctamente", Alert.AlertType.INFORMATION);
                refrescarUsuarios();
            } catch (Exception ex) {
                showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    public void onEditarUsuario() {
        Usuario u = tableUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {showAlert("Error", "Seleccione un usuario", Alert.AlertType.WARNING); return;}
        
        // Mostrar diálogo con campos precargados
        TextField nombreF = new TextField(u.getNombre());
        ChoiceBox<String> tipoD = new ChoiceBox<>();
        tipoD.getItems().addAll("estudiante", "profesor", "administrador");
        tipoD.setValue(u.getTipo());

        VBox vbox = new VBox(5, new Label("Nombre:"), nombreF,
                             new Label("Tipo:"), tipoD);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Editar usuario");
        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                container.getUsuarioService().update(
                    u.getId(),
                    nombreF.getText(),
                    tipoD.getValue());
                showAlert("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
                refrescarUsuarios();
            } catch (Exception ex) {
                showAlert("Error", "Datos inválidos", Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    public void onEliminarUsuario() {
        Usuario u = tableUsuarios.getSelectionModel().getSelectedItem();
        if (u == null) {showAlert("Error", "Seleccione un usuario", Alert.AlertType.WARNING); return;}
        
        boolean exito = container.getUsuarioService().delete(u.getId());
        if(exito){
            usuarios.remove(u);
            showAlert("Éxito", "Usuario eliminado correctamente", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No se puede eliminar tiene prestamos activos", Alert.AlertType.WARNING);
        }
    }

    private void refrescarEquipos() {
        equipos.setAll(container.getEquipoService().listAll());
        tableEquipos.refresh();
    }
    
    private void refrescarUsuarios() {
        usuarios.setAll(container.getUsuarioService().listAll());
        tableUsuarios.refresh();
    }
    
    private void refrescarPrestamos() {
        prestamos.setAll(container.getPrestamoService().listAll());
        tablePrestamos.refresh();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
