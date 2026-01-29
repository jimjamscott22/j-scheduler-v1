package com.jscheduler.ui;

import com.jscheduler.data.AssignmentRepository;
import com.jscheduler.data.CourseRepository;
import com.jscheduler.model.Assignment;
import com.jscheduler.model.Course;
import com.jscheduler.ui.dialog.AssignmentDialogController;
import com.jscheduler.ui.dialog.CourseDialogController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
    private ListView<Course> courseListView;
    @FXML
    private Button addCourseButton;
    @FXML
    private Button editCourseButton;
    @FXML
    private Button deleteCourseButton;

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    private TableView<Assignment> assignmentTable;
    @FXML
    private TableColumn<Assignment, String> titleColumn;
    @FXML
    private TableColumn<Assignment, String> courseColumn;
    @FXML
    private TableColumn<Assignment, String> dueColumn;
    @FXML
    private TableColumn<Assignment, String> deadlineColumn;
    @FXML
    private TableColumn<Assignment, String> statusColumn;
    @FXML
    private TableColumn<Assignment, String> notesColumn;

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

        courseRepository = CourseRepository.getInstance();
        courseListView.setItems(courseRepository.getCourses());
        courseListView.setPlaceholder(new Label("No courses yet."));

        assignmentRepository = AssignmentRepository.getInstance();
        assignmentTable.setItems(assignmentRepository.getAssignments());
        assignmentTable.setPlaceholder(new Label("No assignments yet."));
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                resolveCourseName(cellData.getValue())
        ));
        dueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                formatDate(cellData.getValue().getDueDate())
        ));
        deadlineColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                formatDate(cellData.getValue().getDeadline())
        ));
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        statusLabel.setText("Saved");
        nextDueLabel.setText("Next due: --");
    }

    @FXML
    private void handleAddCourse() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/CourseDialog.fxml"));
            DialogPane dialogPane = loader.load();

            CourseDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Course");
            dialog.setDialogPane(dialogPane);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.setOkClicked(true);
                Course newCourse = controller.getCourse();
                if (newCourse != null) {
                    if (courseRepository.addCourse(newCourse)) {
                        statusLabel.setText("Course added successfully");
                    } else {
                        statusLabel.setText("Failed to add course to database");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error opening course dialog");
        }
    }

    @FXML
    private void handleEditCourse() {
    }

    @FXML
    private void handleDeleteCourse() {
    }

    @FXML
    private void handleAddAssignment() {
        if (courseRepository.getCourses().isEmpty()) {
            statusLabel.setText("Add a course before creating assignments");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ui/AssignmentDialog.fxml"));
            DialogPane dialogPane = loader.load();

            AssignmentDialogController controller = loader.getController();
            Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            controller.setCourseOptions(courseRepository.getCourses(), selectedCourse);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Assignment");
            dialog.setDialogPane(dialogPane);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.setOkClicked(true);
                Assignment newAssignment = controller.getAssignment();
                if (newAssignment != null) {
                    if (assignmentRepository.addAssignment(newAssignment)) {
                        statusLabel.setText("Assignment added successfully");
                    } else {
                        statusLabel.setText("Failed to add assignment to database");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error opening assignment dialog");
        }
    }

    @FXML
    private void handleSaveDetails() {
    }

    @FXML
    private void handleRevertDetails() {
    }

    private String resolveCourseName(Assignment assignment) {
        String courseId = assignment.getCourseId();
        if (courseId == null) {
            return "--";
        }
        for (Course course : courseRepository.getCourses()) {
            if (courseId.equals(course.getId())) {
                return course.getName();
            }
        }
        return "--";
    }

    private String formatDate(java.time.LocalDate date) {
        if (date == null) {
            return "--";
        }
        return dateFormatter.format(date);
    }
}
