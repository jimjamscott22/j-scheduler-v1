package com.jscheduler.ui.dialog;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AssignmentDialogController {

    @FXML
    private ComboBox<String> courseCombo;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextArea notesArea;

    @FXML
    private void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList(
                "Not Started",
                "In Progress",
                "Submitted",
                "Late"
        ));
        statusCombo.getSelectionModel().selectFirst();
    }
}
