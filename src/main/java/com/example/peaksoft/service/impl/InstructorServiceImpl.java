package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.Course;
import com.example.peaksoft.model.Group;
import com.example.peaksoft.model.Instructor;
import com.example.peaksoft.model.Student;
import com.example.peaksoft.repository.CourseRepository;
import com.example.peaksoft.repository.InstructorRepository;
import com.example.peaksoft.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<Instructor> getAllList() {
        return instructorRepository.findAll();
    }

    @Override
    public List<Instructor> getAllInstructor(Long courseId) {
        return instructorRepository.getAllInstructor(courseId);
    }

    @Override
    public void addInstructor(Long id, Instructor instructor) throws IOException {
        Course course = courseRepository.getById(id);
        if (course.getGroups()!=null){
            for (Group group : course.getGroups()) {
                for (Student student: group.getStudents()) {
                    instructor.plus();
                }
            }
        }
        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""), instructor.getFirstName().replace(" ", ""));
        course.addInstructors(instructor);
        instructor.setCourse(course);
        instructorRepository.save(instructor);
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.getById(id);
    }

    @Override
    public void updateInstructor(Instructor instructor, Long id) throws IOException {
        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""), instructor.getFirstName().replace(" ", ""));
        Instructor instructor1 = instructorRepository.getById(id);
        instructor1.setFirstName(instructor.getFirstName());
        instructor1.setLastName(instructor.getLastName());
        instructor1.setEmail(instructor.getEmail());
        instructor1.setSpecialization(instructor.getSpecialization());
        instructor1.setPhoneNumber(instructor.getPhoneNumber());
        instructorRepository.save(instructor1);
    }

    @Override
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) throws IOException {
        Instructor instructor = instructorRepository.getById(instructorId);
        Course course = courseRepository.getById(courseId);
        if (course.getInstructors()!=null){
            for (Instructor g : course.getInstructors()) {
                if (g.getId() == instructorId) {
                    throw new IOException("This instructor already exists!");
                }
            }
        }
        for (Group g:instructor.getCourse().getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.minus();
            }
        }
        for (Group g: course.getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.plus();
            }
        }
        instructor.getCourse().getInstructors().remove(instructor);
        instructor.setCourse(course);
        course.addInstructors(instructor);
        instructorRepository.save(instructor);
        courseRepository.save(course);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length()>2 && lastName.length()>2) {
            for (Character i : firstName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В имени инструктора нельзя вставлять цифры");
                }
            }

            for (Character i : lastName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В фамилию инструктора нельзя вставлять цифры");
                }
            }
        } else {
            throw new IOException("В имени или фамилии инструктора должно быть как минимум 3 буквы");
        }

        if (phone.length()==13
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '9'
                && phone.charAt(2) == '9'
                && phone.charAt(3) == '6'){
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter!=0){
                    if (!Character.isDigit(i)) {
                        throw new IOException("Формат номера не правильный");
                    }
                }
                counter++;
            }
        }else {
            throw new IOException("Формат номера не правильный");
        }
    }
}
