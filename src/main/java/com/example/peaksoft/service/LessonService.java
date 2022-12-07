package com.example.peaksoft.service;

import com.example.peaksoft.model.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> getAllLessons(Long id);

    void addLesson(Long id, Lesson lesson);

    Lesson getLessonById(Long id);

    void updateLesson(Lesson lesson, Long id);

    void deleteLesson(Long id);
}
