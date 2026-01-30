package com.jscheduler.service;

import com.google.gson.*;
import com.jscheduler.model.Assignment;
import com.jscheduler.model.AssignmentStatus;
import com.jscheduler.model.Course;
import com.jscheduler.model.Semester;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonPersistenceService {
    private static final String DATA_FILE = "semester.json";
    private final Path dataPath;
    private final Gson gson;

    public JsonPersistenceService() {
        String userHome = System.getProperty("user.home");
        Path appDir = Paths.get(userHome, ".jscheduler");
        this.dataPath = appDir.resolve(DATA_FILE);

        try {
            Files.createDirectories(appDir);
        } catch (IOException e) {
            System.err.println("Could not create app directory: " + e.getMessage());
        }

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public Semester load() {
        if (!Files.exists(dataPath)) {
            return null;
        }

        try (Reader reader = Files.newBufferedReader(dataPath)) {
            SemesterDto dto = gson.fromJson(reader, SemesterDto.class);
            return convertFromDto(dto);
        } catch (IOException | JsonParseException e) {
            System.err.println("Could not load data: " + e.getMessage());
            try {
                Files.move(dataPath, dataPath.resolveSibling("semester.json.backup"));
                System.err.println("Corrupted file backed up to semester.json.backup");
            } catch (IOException ex) {
                System.err.println("Could not backup corrupted file: " + ex.getMessage());
            }
            return null;
        }
    }

    public void save(Semester semester) {
        SemesterDto dto = convertToDto(semester);

        try (Writer writer = Files.newBufferedWriter(dataPath)) {
            gson.toJson(dto, writer);
        } catch (IOException e) {
            System.err.println("Could not save data: " + e.getMessage());
        }
    }

    private static class SemesterDto {
        String name;
        List<CourseDto> courses;
        List<AssignmentDto> assignments;
    }

    private static class CourseDto {
        String id;
        String name;
        String description;
        String professor;
        String semester;
    }

    private static class AssignmentDto {
        String id;
        String courseId;
        String title;
        String description;
        LocalDate dueDate;
        LocalDate submissionDeadline;
        String status;
        String notes;
    }

    private SemesterDto convertToDto(Semester semester) {
        SemesterDto dto = new SemesterDto();
        dto.name = semester.getName();
        dto.courses = new ArrayList<>();
        dto.assignments = new ArrayList<>();

        for (Course course : semester.getCourses()) {
            CourseDto courseDto = new CourseDto();
            courseDto.id = course.getId();
            courseDto.name = course.getName();
            courseDto.description = course.getDescription();
            courseDto.professor = course.getProfessor();
            courseDto.semester = course.getSemester();
            dto.courses.add(courseDto);
        }

        for (Assignment assignment : semester.getAssignments()) {
            AssignmentDto assignmentDto = new AssignmentDto();
            assignmentDto.id = assignment.getId();
            assignmentDto.courseId = assignment.getCourseId();
            assignmentDto.title = assignment.getTitle();
            assignmentDto.description = assignment.getDescription();
            assignmentDto.dueDate = assignment.getDueDate();
            assignmentDto.submissionDeadline = assignment.getSubmissionDeadline();
            assignmentDto.status = assignment.getStatus() != null ? assignment.getStatus().getDisplayName() : "Not Started";
            assignmentDto.notes = assignment.getNotes();
            dto.assignments.add(assignmentDto);
        }

        return dto;
    }

    private Semester convertFromDto(SemesterDto dto) {
        Semester semester = new Semester(dto.name);

        if (dto.courses != null) {
            for (CourseDto courseDto : dto.courses) {
                Course course = new Course(
                        courseDto.id,
                        courseDto.name,
                        courseDto.description,
                        courseDto.professor,
                        courseDto.semester
                );
                semester.addCourse(course);
            }
        }

        if (dto.assignments != null) {
            for (AssignmentDto assignmentDto : dto.assignments) {
                Assignment assignment = new Assignment(
                        assignmentDto.id,
                        assignmentDto.courseId,
                        assignmentDto.title,
                        assignmentDto.description,
                        assignmentDto.dueDate,
                        assignmentDto.submissionDeadline,
                        AssignmentStatus.fromString(assignmentDto.status),
                        assignmentDto.notes
                );
                semester.addAssignment(assignment);
            }
        }

        return semester;
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
            return date != null ? new JsonPrimitive(date.toString()) : null;
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
            return json != null && !json.isJsonNull() ? LocalDate.parse(json.getAsString()) : null;
        }
    }
}
