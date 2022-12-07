package com.example.peaksoft.repository;

import com.example.peaksoft.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query(value = "select c from Lesson c where c.course.id = :courseId")
    List<Lesson> getAllLessons(Long courseId);
}