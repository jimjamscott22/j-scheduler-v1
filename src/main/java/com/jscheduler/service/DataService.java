package com.jscheduler.service;

import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.model.Course;
import com.jscheduler.model.Semester;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataService {
    private static DataService instance;

    private final JsonPersistenceService persistenceService;
    private Semester currentSemester;

    private final ObservableList<Course> courses;
    private final ObservableList<Assignment> assignments;

    private DataService() {
        this.persistenceService = new JsonPersistenceService();
        this.courses = FXCollections.observableArrayList();
        this.assignments = FXCollections.observableArrayList();
        loadData();
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public void addCourse(Course course) {
        currentSemester.addCourse(course);
        courses.add(course);
        saveData();
    }

    public void updateCourse(Course course) {
        saveData();
    }

    public void deleteCourse(Course course) {
        List<Assignment> toRemove = new ArrayList<>();
        for (Assignment a : assignments) {
            if (a.getCourseId().equals(course.getId())) {
                toRemove.add(a);
            }
        }
        assignments.removeAll(toRemove);

        currentSemester.removeCourse(course.getId());
        courses.remove(course);
        saveData();
    }

    public void addAssignment(Assignment assignment) {
        Course course = findCourseById(assignment.getCourseId());
        if (course != null) {
            assignment.setCourseName(course.getName());
        }
        currentSemester.addAssignment(assignment);
        assignments.add(assignment);
        saveData();
    }

    public void updateAssignment(Assignment assignment) {
        saveData();
    }

    public void deleteAssignment(Assignment assignment) {
        currentSemester.removeAssignment(assignment.getId());
        assignments.remove(assignment);
        saveData();
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    public ObservableList<Assignment> getAssignments() {
        return assignments;
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public Course findCourseById(String id) {
        return courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void loadData() {
        currentSemester = persistenceService.load();
        if (currentSemester == null) {
            currentSemester = new Semester("Fall 2026");
        }

        courses.setAll(currentSemester.getCourses());
        assignments.setAll(currentSemester.getAssignments());

        for (Assignment a : assignments) {
            Course c = findCourseById(a.getCourseId());
            if (c != null) {
                a.setCourseName(c.getName());
            } else {
                a.setCourseName("[Deleted Course]");
            }
        }
    }

    public void saveData() {
        currentSemester.setCourses(new ArrayList<>(courses));
        currentSemester.setAssignments(new ArrayList<>(assignments));
        persistenceService.save(currentSemester);
    }

    public Assignment getNextDueAssignment() {
        LocalDate today = LocalDate.now();
        return assignments.stream()
                .filter(a -> a.getDueDate() != null && !a.getDueDate().isBefore(today))
                .filter(a -> a.getStatus() != AssignmentStatus.SUBMITTED)
                .min(Comparator.comparing(Assignment::getDueDate))
                .orElse(null);
    }
}
