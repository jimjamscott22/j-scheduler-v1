package com.jscheduler.data;

import com.jscheduler.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CourseRepository {
    private static CourseRepository instance;
    private final ObservableList<Course> courses;

    private CourseRepository() {
        courses = FXCollections.observableArrayList();
    }

    public static CourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public void updateCourse(Course oldCourse, Course newCourse) {
        int index = courses.indexOf(oldCourse);
        if (index >= 0) {
            courses.set(index, newCourse);
        }
    }
}
