package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.Course;
import com.example.peaksoft.model.Group;
import com.example.peaksoft.model.Instructor;
import com.example.peaksoft.model.Student;
import com.example.peaksoft.repository.GroupRepository;
import com.example.peaksoft.repository.StudentRepository;
import com.example.peaksoft.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    @Override
    public List<Student> getAllListStudent() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudents(Long id) {
        return studentRepository.getAllListStudent(id);
    }

    @Override
    public void addStudent(Long id, Student student) throws IOException {
        List<Student> students = studentRepository.findAll();
        for (Student i : students) {
            if (i.getEmail().equals(student.getEmail())) {
                throw new IOException("Student with email already exists!");
            }
        }

        Group group = groupRepository.getById( id);
        group.addStudent(student);
        student.setGroups(group);
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }

        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.getById(id);
    }

    @Override
    public void updateStudent(Student student, Long id) throws IOException {
        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        Student student1 = studentRepository.getById(id);
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setPhoneNumber(student.getPhoneNumber());
        student1.setEmail(student.getEmail());
        student1.setStudyFormat(student.getStudyFormat());
        studentRepository.save(student1);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.getById(id);
        student.getGroups().getCompany().minusStudent();
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i:c.getInstructors()) {
                i.minus();
            }
        }
        student.setGroups(null);
        studentRepository.delete(student);
    }

    @Override
    public void assignStudent(Long groupId, Long studentId) throws IOException {
        Student student = studentRepository.getById(studentId);
        Group group = groupRepository.getById(groupId);

        if (group.getStudents()!=null){
            for (Student g : group.getStudents()) {
                if (g.getId() == studentId) {
                    throw new IOException("This student already exists!");
                }
            }
        }
        for (Course c: student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.minus();
            }
        }
        for (Course c: group.getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }
        student.getGroups().getStudents().remove(student);
        group.assignStudent(student);
        student.setGroups(group);
        studentRepository.save(student);
        groupRepository.save(group);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length() > 2 && lastName.length() > 2) {
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

        if (phone.length() == 13
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '9'
                && phone.charAt(2) == '9'
                && phone.charAt(3) == '6') {
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter != 0) {
                    if (!Character.isDigit(i)) {
                        throw new IOException("Формат номера не правильный");
                    }
                }
                counter++;
            }
        } else {
            throw new IOException("Формат номера не правильный");
        }
    }
}
