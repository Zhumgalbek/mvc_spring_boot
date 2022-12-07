package com.example.peaksoft.repository;

import com.example.peaksoft.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "select c from Student c where c.groups.id = :groupId")
    List<Student> getAllListStudent(Long groupId);
}
