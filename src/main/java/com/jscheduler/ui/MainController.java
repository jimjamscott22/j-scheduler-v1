package com.jscheduler.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private ComboBox<String> semesterCombo;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> statusFilterCombo;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private Button addAssignmentButton;

    @FXML
    private ListView<String> courseListView;
    @FXML
    private Button addCourseButton;
    @FXML
    private Button editCourseButton;
    @FXML
    private Button deleteCourseButton;

    @FXML
    private TableView<Object> assignmentTable;
    @FXML
    private TableColumn<Object, String> titleColumn;
    @FXML
    private TableColumn<Object, String> courseColumn;
    @FXML
    private TableColumn<Object, String> dueColumn;
    @FXML
    private TableColumn<Object, String> deadlineColumn;
    @FXML
    private TableColumn<Object, String> statusColumn;
    @FXML
    private TableColumn<Object, String> notesColumn;

    @FXML
    private TextField detailTitleField;
    @FXML
    private DatePicker detailDueDatePicker;
    @FXML
    private DatePicker detailDeadlineDatePicker;
    @FXML
    private ComboBox<String> detailStatusComboBox;
    @FXML
    private TextArea detailNotesArea;
    @FXML
    private Button detailSaveButton;
    @FXML
    private Button detailRevertButton;

    @FXML
    private Label statusLabel;
    @FXML
    private Label nextDueLabel;

    @FXML
    private void initialize() {
        semesterCombo.setItems(FXCollections.observableArrayList(
                "Fall 2026",
                "Spring 2027"
        ));
        semesterCombo.getSelectionModel().selectFirst();

        statusFilterCombo.setItems(FXCollections.observableArrayList(
                "Any",
                "Not Started",
                "In Progress",
                "Submitted",
                "Late"
        ));
        statusFilterCombo.getSelectionModel().selectFirst();

        detailStatusComboBox.setItems(FXCollections.observableArrayList(
                "Not Started",
                "In Progress",
                "Submitted",
                "Late"
        ));
        detailStatusComboBox.getSelectionModel().selectFirst();

        courseListView.setPlaceholder(new Label("No courses yet."));
        assignmentTable.setPlaceholder(new Label("No assignments yet."));
        statusLabel.setText("Saved");
        nextDueLabel.setText("Next due: --");
    }

    @FXML
    private void handleAddCourse() {
    }

    @FXML
    private void handleEditCourse() {
    }

    @FXML
    private void handleDeleteCourse() {
    }

    @FXML
    private void handleAddAssignment() {
    }

    @FXML
    private void handleSaveDetails() {
    }

    @FXML
    private void handleRevertDetails() {
    }
}
