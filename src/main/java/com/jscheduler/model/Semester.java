package com.jscheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Semester {
    private String name;
    private List<Course> courses;
    private List<Assignment> assignments;

    public Semester(String name) {
        this.name = name;
        this.courses = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Course findCourseById(String id) {
        return courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Assignment findAssignmentById(String id) {
        return assignments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(String courseId) {
        courses.removeIf(c -> c.getId().equals(courseId));
        assignments.removeIf(a -> a.getCourseId().equals(courseId));
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(String assignmentId) {
        assignments.removeIf(a -> a.getId().equals(assignmentId));
    }
}
