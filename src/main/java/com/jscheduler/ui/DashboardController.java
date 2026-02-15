package com.jscheduler.ui;

import com.jscheduler.data.AssignmentRepository;
import com.jscheduler.data.CourseRepository;
import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.model.Course;
import com.jscheduler.service.GPACalculator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {

    @FXML
    private Label totalCoursesLabel;
    @FXML
    private Label totalAssignmentsLabel;
    @FXML
    private Label completedAssignmentsLabel;
    @FXML
    private Label pendingAssignmentsLabel;
    @FXML
    private Label upcomingThisWeekLabel;
    @FXML
    private Label overdueLabel;
    @FXML
    private Label currentGPALabel;
    @FXML
    private Label averageGradeLabel;

    @FXML
    private PieChart statusPieChart;
    @FXML
    private BarChart<String, Number> courseWorkloadChart;
    @FXML
    private BarChart<String, Number> priorityChart;

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;

    @FXML
    private void initialize() {
        courseRepository = CourseRepository.getInstance();
        assignmentRepository = AssignmentRepository.getInstance();

        refreshDashboard();
    }

    public void refreshDashboard() {
        updateStatistics();
        updateStatusPieChart();
        updateCourseWorkloadChart();
        updatePriorityChart();
    }

    private void updateStatistics() {
        ObservableList<Course> courses = courseRepository.getCourses();
        ObservableList<Assignment> assignments = assignmentRepository.getAssignments();

        // Basic counts
        totalCoursesLabel.setText(String.valueOf(courses.size()));
        totalAssignmentsLabel.setText(String.valueOf(assignments.size()));

        // Assignment status counts
        long completed = assignments.stream()
                .filter(a -> a.getStatus() == AssignmentStatus.SUBMITTED)
                .count();
        long pending = assignments.stream()
                .filter(a -> a.getStatus() == AssignmentStatus.NOT_STARTED ||
                            a.getStatus() == AssignmentStatus.IN_PROGRESS)
                .count();

        completedAssignmentsLabel.setText(String.valueOf(completed));
        pendingAssignmentsLabel.setText(String.valueOf(pending));

        // Upcoming this week
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(7);
        long upcomingThisWeek = assignments.stream()
                .filter(a -> a.getDueDate() != null)
                .filter(a -> !a.getDueDate().isBefore(today) && !a.getDueDate().isAfter(endOfWeek))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count();
        upcomingThisWeekLabel.setText(String.valueOf(upcomingThisWeek));

        // Overdue
        long overdue = assignments.stream()
                .filter(a -> a.getDueDate() != null)
                .filter(a -> a.getDueDate().isBefore(today))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count();
        overdueLabel.setText(String.valueOf(overdue));

        // GPA calculation
        double gpa = GPACalculator.calculateGPA(courses);
        currentGPALabel.setText(gpa > 0 ? String.format("%.2f", gpa) : "N/A");

        // Average grade from assignments
        double avgGrade = assignments.stream()
                .filter(a -> a.getGrade() != null)
                .mapToDouble(Assignment::getGrade)
                .average()
                .orElse(0.0);
        averageGradeLabel.setText(avgGrade > 0 ? String.format("%.1f%%", avgGrade) : "N/A");
    }

    private void updateStatusPieChart() {
        ObservableList<Assignment> assignments = assignmentRepository.getAssignments();

        Map<AssignmentStatus, Long> statusCounts = new HashMap<>();
        for (AssignmentStatus status : AssignmentStatus.values()) {
            long count = assignments.stream()
                    .filter(a -> a.getStatus() == status)
                    .count();
            if (count > 0) {
                statusCounts.put(status, count);
            }
        }

        statusPieChart.getData().clear();
        statusCounts.forEach((status, count) -> {
            PieChart.Data slice = new PieChart.Data(status.getDisplayName() + " (" + count + ")", count);
            statusPieChart.getData().add(slice);
        });
    }

    private void updateCourseWorkloadChart() {
        ObservableList<Course> courses = courseRepository.getCourses();
        ObservableList<Assignment> assignments = assignmentRepository.getAssignments();

        Map<String, Long> workloadMap = new HashMap<>();
        for (Course course : courses) {
            long count = assignments.stream()
                    .filter(a -> a.getCourseId().equals(course.getId()))
                    .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                    .count();
            if (count > 0) {
                workloadMap.put(course.getName(), count);
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pending Assignments");

        workloadMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by count descending
                .limit(10) // Show top 10
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });

        courseWorkloadChart.getData().clear();
        courseWorkloadChart.getData().add(series);
    }

    private void updatePriorityChart() {
        ObservableList<Assignment> assignments = assignmentRepository.getAssignments();

        Map<String, Long> priorityCounts = new HashMap<>();
        priorityCounts.put("Urgent", assignments.stream()
                .filter(a -> a.getPriority() != null && "Urgent".equals(a.getPriority().getDisplayName()))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count());
        priorityCounts.put("High", assignments.stream()
                .filter(a -> a.getPriority() != null && "High".equals(a.getPriority().getDisplayName()))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count());
        priorityCounts.put("Medium", assignments.stream()
                .filter(a -> a.getPriority() != null && "Medium".equals(a.getPriority().getDisplayName()))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count());
        priorityCounts.put("Low", assignments.stream()
                .filter(a -> a.getPriority() != null && "Low".equals(a.getPriority().getDisplayName()))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .count());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pending Assignments by Priority");

        priorityCounts.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });

        priorityChart.getData().clear();
        priorityChart.getData().add(series);
    }
}

