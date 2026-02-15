package com.jscheduler.ui;

import com.jscheduler.data.AssignmentRepository;
import com.jscheduler.data.CourseRepository;
import com.jscheduler.model.Assignment;
import com.jscheduler.model.Course;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.ui.dialog.AssignmentDialogController;
import com.jscheduler.ui.dialog.CourseDialogController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

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
    private Button showAllAssignmentsButton;

    @FXML
    private ListView<Course> courseListView;
    @FXML
    private Button addCourseButton;
    @FXML
    private Button editCourseButton;
    @FXML
    private Button deleteCourseButton;

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

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private Assignment currentAssignment;
    private FilteredList<Assignment> filteredAssignments;

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
    private Button editAssignmentButton;
    @FXML
    private Button deleteAssignmentButton;
    @FXML
    private Button detailDeleteButton;

    @FXML
    private Label statusLabel;
    @FXML
    private Label nextDueLabel;

    @FXML
    private void initialize() {
        courseRepository = CourseRepository.getInstance();
        assignmentRepository = AssignmentRepository.getInstance();

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

        courseListView.setItems(courseRepository.getCourses());
        courseListView.setPlaceholder(new Label("No courses yet."));
        courseListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> applyCourseFilter(newSelection)
        );

        // Double-click to view/edit course details
        courseListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
                if (selectedCourse != null) {
                    handleEditCourse();
                }
            }
        });

        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
        dueColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> {
            var date = cellData.getValue().getDueDate();
            return date != null ? date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "";
        }, cellData.getValue().dueDateProperty()));
        deadlineColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> {
            var date = cellData.getValue().getSubmissionDeadline();
            return date != null ? date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : "";
        }, cellData.getValue().submissionDeadlineProperty()));
        statusColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> {
            var status = cellData.getValue().getStatus();
            return status != null ? status.getDisplayName() : "";
        }, cellData.getValue().statusProperty()));

        statusColumn.setCellFactory(column -> new TableCell<Assignment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    getStyleClass().removeAll("status-not-started", "status-in-progress", "status-submitted", "status-late");
                } else {
                    setText(item);
                    getStyleClass().removeAll("status-not-started", "status-in-progress", "status-submitted", "status-late");
                    
                    AssignmentStatus status = AssignmentStatus.fromString(item);
                    switch (status) {
                        case NOT_STARTED -> getStyleClass().add("status-not-started");
                        case IN_PROGRESS -> getStyleClass().add("status-in-progress");
                        case SUBMITTED -> getStyleClass().add("status-submitted");
                        case LATE -> getStyleClass().add("status-late");
                    }
                }
            }
        });
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        filteredAssignments = new FilteredList<>(assignmentRepository.getAssignments(), assignment -> true);
        assignmentTable.setItems(filteredAssignments);
        assignmentTable.setPlaceholder(new Label("No assignments yet."));

        assignmentTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                populateDetailPanel(newSelection);
                updateAssignmentButtons(newSelection);
            }
        );

        // Double-click to edit assignment
        assignmentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Assignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
                if (selectedAssignment != null) {
                    handleEditAssignment();
                }
            }
        });

        detailSaveButton.setDisable(true);
        detailRevertButton.setDisable(true);
        if (editAssignmentButton != null) editAssignmentButton.setDisable(true);
        if (deleteAssignmentButton != null) deleteAssignmentButton.setDisable(true);
        if (detailDeleteButton != null) detailDeleteButton.setDisable(true);

        statusLabel.setText("Saved");
        updateNextDueLabel();
    }

    @FXML
    private void handleAddCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CourseDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CourseDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Course");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Course course = controller.getResult();
                if (courseRepository.addCourse(course)) {
                    statusLabel.setText("Course added: " + course.getName());
                } else {
                    showError("Error", "Failed to add course to database");
                }
            }
        } catch (IOException e) {
            showError("Error", "Could not open course dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditCourse() {
        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showError("No Selection", "Please select a course to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CourseDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CourseDialogController controller = loader.getController();
            controller.setData(selectedCourse);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Course");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Course updatedCourse = controller.getResult();
                if (courseRepository.updateCourse(selectedCourse, updatedCourse)) {
                    courseListView.refresh();
                    statusLabel.setText("Course updated: " + selectedCourse.getName());
                } else {
                    showError("Error", "Failed to update course in database");
                }
            }
        } catch (IOException e) {
            showError("Error", "Could not open course dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showError("No Selection", "Please select a course to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Course");
        confirmation.setHeaderText("Delete course: " + selectedCourse.getName() + "?");
        confirmation.setContentText("This will also delete all assignments for this course.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (courseRepository.removeCourse(selectedCourse)) {
                statusLabel.setText("Course deleted: " + selectedCourse.getName());
            } else {
                showError("Error", "Failed to delete course from database");
            }
        }
    }

    private void updateAssignmentButtons(Assignment assignment) {
        boolean disabled = (assignment == null);
        if (editAssignmentButton != null) editAssignmentButton.setDisable(disabled);
        if (deleteAssignmentButton != null) deleteAssignmentButton.setDisable(disabled);
        if (detailDeleteButton != null) detailDeleteButton.setDisable(disabled);
    }

    @FXML
    private void handleDeleteAssignment() {
        Assignment assignmentToDelete = currentAssignment;
        // If triggered from the table context, we might want the table selection
        if (assignmentToDelete == null) {
            assignmentToDelete = assignmentTable.getSelectionModel().getSelectedItem();
        }

        if (assignmentToDelete == null) {
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Assignment");
        confirmation.setHeaderText("Delete assignment: " + assignmentToDelete.getTitle() + "?");
        confirmation.setContentText("Are you sure you want to delete this assignment?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (assignmentRepository.removeAssignment(assignmentToDelete)) {
                statusLabel.setText("Assignment deleted: " + assignmentToDelete.getTitle());
                if (assignmentToDelete == currentAssignment) {
                    clearDetailPanel();
                }
                updateNextDueLabel();
            } else {
                showError("Error", "Failed to delete assignment from database");
            }
        }
    }

    @FXML
    private void handleEditAssignment() {
        Assignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
        if (selectedAssignment == null) {
            selectedAssignment = currentAssignment;
        }

        if (selectedAssignment == null) {
            showError("No Selection", "Please select an assignment to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AssignmentDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AssignmentDialogController controller = loader.getController();
            controller.setCourses(courseRepository.getCourses());
            controller.setData(selectedAssignment);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Assignment");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Assignment updatedAssignment = controller.getResult();
                
                // Update properties of the existing assignment object
                selectedAssignment.setCourseId(updatedAssignment.getCourseId());
                selectedAssignment.setTitle(updatedAssignment.getTitle());
                selectedAssignment.setDescription(updatedAssignment.getDescription());
                selectedAssignment.setDueDate(updatedAssignment.getDueDate());
                selectedAssignment.setSubmissionDeadline(updatedAssignment.getSubmissionDeadline());
                selectedAssignment.setStatus(updatedAssignment.getStatus());
                selectedAssignment.setNotes(updatedAssignment.getNotes());
                
                Course course = findCourseById(selectedAssignment.getCourseId());
                if (course != null) {
                    selectedAssignment.setCourseName(course.getName());
                }

                if (assignmentRepository.updateAssignment(selectedAssignment)) {
                    assignmentTable.refresh();
                    if (selectedAssignment == currentAssignment) {
                        populateDetailPanel(selectedAssignment);
                    }
                    statusLabel.setText("Assignment updated: " + selectedAssignment.getTitle());
                    updateNextDueLabel();
                } else {
                    showError("Error", "Failed to update assignment in database");
                }
            }
        } catch (IOException e) {
            showError("Error", "Could not open assignment dialog: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateNextDueLabel() {
        Assignment nextAssignment = getNextDueAssignment();
        if (nextAssignment != null) {
            String dateStr = nextAssignment.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd"));
            nextDueLabel.setText("Next due: " + nextAssignment.getTitle() + " (" + dateStr + ")");
        } else {
            nextDueLabel.setText("Next due: --");
        }
    }

    private Assignment getNextDueAssignment() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return assignmentRepository.getAssignments().stream()
                .filter(a -> a.getDueDate() != null && !a.getDueDate().isBefore(today))
                .filter(a -> a.getStatus() != com.jscheduler.model.AssignmentStatus.SUBMITTED)
                .min(java.util.Comparator.comparing(Assignment::getDueDate))
                .orElse(null);
    }

    @FXML
    private void handleAddAssignment() {
        if (courseRepository.getCourses().isEmpty()) {
            showError("No Courses", "Please add a course first before creating assignments.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AssignmentDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AssignmentDialogController controller = loader.getController();
            controller.setCourses(courseRepository.getCourses());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Assignment");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Assignment assignment = controller.getResult();
                Course course = findCourseById(assignment.getCourseId());
                if (course != null) {
                    assignment.setCourseName(course.getName());
                }
                if (assignmentRepository.addAssignment(assignment)) {
                    statusLabel.setText("Assignment added: " + assignment.getTitle());
                    updateNextDueLabel();
                } else {
                    showError("Error", "Failed to add assignment to database");
                }
            }
        } catch (IOException e) {
            showError("Error", "Could not open assignment dialog: " + e.getMessage());
        }
    }

    private Course findCourseById(String id) {
        return courseRepository.getCourses().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void handleSaveDetails() {
        if (currentAssignment == null) {
            return;
        }

        currentAssignment.setTitle(detailTitleField.getText());
        currentAssignment.setDueDate(detailDueDatePicker.getValue());
        currentAssignment.setSubmissionDeadline(detailDeadlineDatePicker.getValue());
        currentAssignment.setStatus(com.jscheduler.model.AssignmentStatus.fromString(detailStatusComboBox.getValue()));
        currentAssignment.setNotes(detailNotesArea.getText());

        if (assignmentRepository.updateAssignment(currentAssignment)) {
            assignmentTable.refresh();
            statusLabel.setText("Saved assignment: " + currentAssignment.getTitle());
            updateNextDueLabel();
        } else {
            showError("Error", "Failed to update assignment in database");
        }
    }

    @FXML
    private void handleRevertDetails() {
        populateDetailPanel(currentAssignment);
        statusLabel.setText("Reverted changes");
    }

    private void populateDetailPanel(Assignment assignment) {
        if (assignment == null) {
            clearDetailPanel();
            return;
        }

        currentAssignment = assignment;
        detailTitleField.setText(assignment.getTitle());
        detailDueDatePicker.setValue(assignment.getDueDate());
        detailDeadlineDatePicker.setValue(assignment.getSubmissionDeadline());
        detailStatusComboBox.setValue(assignment.getStatus() != null ? assignment.getStatus().getDisplayName() : "Not Started");
        detailNotesArea.setText(assignment.getNotes());

        detailSaveButton.setDisable(false);
        detailRevertButton.setDisable(false);
        if (detailDeleteButton != null) detailDeleteButton.setDisable(false);
    }

    private void clearDetailPanel() {
        currentAssignment = null;
        detailTitleField.clear();
        detailDueDatePicker.setValue(null);
        detailDeadlineDatePicker.setValue(null);
        detailStatusComboBox.getSelectionModel().selectFirst();
        detailNotesArea.clear();

        detailSaveButton.setDisable(true);
        detailRevertButton.setDisable(true);
        if (detailDeleteButton != null) detailDeleteButton.setDisable(true);
    }

    @FXML
    private void handleShowAllAssignments() {
        courseListView.getSelectionModel().clearSelection();
        applyCourseFilter(null);
    }

    private void applyCourseFilter(Course selectedCourse) {
        String selectedCourseId = selectedCourse != null ? selectedCourse.getId() : null;
        filteredAssignments.setPredicate(assignment -> selectedCourseId == null
                || assignment.getCourseId().equals(selectedCourseId));
        assignmentTable.getSelectionModel().clearSelection();
        clearDetailPanel();
        updateAssignmentButtons(null);
    }
}
