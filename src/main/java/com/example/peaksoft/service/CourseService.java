package com.example.peaksoft.service;

import com.example.peaksoft.model.Course;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses(Long id);

    void addCourse(Long id, Course course) throws IOException;

    Course getCourseById(Long id);

    void updateCourse(Course course, Long id) throws IOException;

    void deleteCourse(Long id);
}
