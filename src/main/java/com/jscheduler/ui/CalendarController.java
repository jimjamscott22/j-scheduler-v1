package com.jscheduler.ui;

import com.jscheduler.data.AssignmentRepository;
import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarController {

    @FXML
    private Label monthYearLabel;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Button prevMonthButton;

    @FXML
    private Button nextMonthButton;

    @FXML
    private Button todayButton;

    private AssignmentRepository assignmentRepository;
    private YearMonth currentYearMonth;

    @FXML
    private void initialize() {
        assignmentRepository = AssignmentRepository.getInstance();
        currentYearMonth = YearMonth.now();
        
        // Listen for changes in assignments
        assignmentRepository.getAssignments().addListener(
            (javafx.collections.ListChangeListener.Change<? extends Assignment> c) -> {
                refreshCalendar();
            }
        );
        
        refreshCalendar();
    }

    @FXML
    private void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        refreshCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        refreshCalendar();
    }

    @FXML
    private void handleToday() {
        currentYearMonth = YearMonth.now();
        refreshCalendar();
    }

    private void refreshCalendar() {
        calendarGrid.getChildren().clear();
        
        // Update month/year label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthYearLabel.setText(currentYearMonth.format(formatter));
        
        // Add day headers
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < dayNames.length; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.getStyleClass().add("calendar-day-header");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }
        
        // Get first day of month and number of days
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        
        LocalDate today = LocalDate.now();
        
        // Add calendar cells
        int currentDay = 1;
        for (int row = 1; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 1 && col < startDayOfWeek) {
                    // Empty cell before month starts
                    VBox emptyCell = new VBox();
                    emptyCell.getStyleClass().add("calendar-cell");
                    emptyCell.getStyleClass().add("calendar-cell-empty");
                    calendarGrid.add(emptyCell, col, row);
                } else if (currentDay > daysInMonth) {
                    // Empty cell after month ends
                    VBox emptyCell = new VBox();
                    emptyCell.getStyleClass().add("calendar-cell");
                    emptyCell.getStyleClass().add("calendar-cell-empty");
                    calendarGrid.add(emptyCell, col, row);
                } else {
                    // Valid day cell
                    LocalDate date = currentYearMonth.atDay(currentDay);
                    VBox dayCell = createDayCell(date, today);
                    calendarGrid.add(dayCell, col, row);
                    currentDay++;
                }
            }
            if (currentDay > daysInMonth) {
                break;
            }
        }
    }

    private VBox createDayCell(LocalDate date, LocalDate today) {
        VBox cell = new VBox(5);
        cell.getStyleClass().add("calendar-cell");
        
        // Add today styling
        if (date.equals(today)) {
            cell.getStyleClass().add("calendar-cell-today");
        }
        
        // Day number label
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.getStyleClass().add("calendar-day-number");
        cell.getChildren().add(dayLabel);
        
        // Get assignments for this date
        List<Assignment> assignments = assignmentRepository.getAssignments().stream()
            .filter(a -> a.getDueDate() != null && a.getDueDate().equals(date))
            .collect(Collectors.toList());
        
        if (!assignments.isEmpty()) {
            cell.getStyleClass().add("calendar-cell-has-assignments");
            
            // Create assignment indicators
            for (Assignment assignment : assignments) {
                if (cell.getChildren().size() < 5) { // Limit to 4 assignments displayed (+ day number)
                    Label assignmentLabel = new Label();
                    assignmentLabel.setText(assignment.getTitle());
                    assignmentLabel.getStyleClass().add("calendar-assignment");
                    
                    // Add status-specific styling
                    if (assignment.getStatus() != null) {
                        switch (assignment.getStatus()) {
                            case NOT_STARTED -> assignmentLabel.getStyleClass().add("status-not-started");
                            case IN_PROGRESS -> assignmentLabel.getStyleClass().add("status-in-progress");
                            case SUBMITTED -> assignmentLabel.getStyleClass().add("status-submitted");
                            case LATE -> assignmentLabel.getStyleClass().add("status-late");
                        }
                    }
                    
                    // Add tooltip with details
                    String tooltipText = String.format(
                        "%s\nCourse: %s\nStatus: %s%s",
                        assignment.getTitle(),
                        assignment.getCourseName(),
                        assignment.getStatus() != null ? assignment.getStatus().getDisplayName() : "Not Started",
                        assignment.getSubmissionDeadline() != null ? 
                            "\nDeadline: " + assignment.getSubmissionDeadline().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) : ""
                    );
                    
                    Tooltip tooltip = new Tooltip(tooltipText);
                    tooltip.setShowDelay(Duration.millis(200));
                    Tooltip.install(assignmentLabel, tooltip);
                    
                    cell.getChildren().add(assignmentLabel);
                }
            }
            
            // If more assignments than can be displayed
            if (assignments.size() > 4) {
                Label moreLabel = new Label("+" + (assignments.size() - 4) + " more");
                moreLabel.getStyleClass().add("calendar-more-label");
                cell.getChildren().add(moreLabel);
            }
        }
        
        return cell;
    }
}
