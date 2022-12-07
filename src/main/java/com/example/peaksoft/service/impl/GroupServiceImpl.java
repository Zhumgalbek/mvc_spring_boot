package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.*;
import com.example.peaksoft.repository.CompanyRepository;
import com.example.peaksoft.repository.CourseRepository;
import com.example.peaksoft.repository.GroupRepository;
import com.example.peaksoft.repository.StudentRepository;
import com.example.peaksoft.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;


    @Override
    public List<Group> getAllGroup(Long id) {
        return groupRepository.getAllGroup(id);
    }

    @Override
    public List<Group> getAllGroupsByCourseId(Long courseId) {
        return courseRepository.getReferenceById(courseId).getGroups();
    }

    @Override
    public void addGroupByCourseId(Long id, Long courseId, Group group) {
        if (group.getImage().length()<15){
            group.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6ZKLU-ncmqfKAVjB03AcWFuB2mdVuYGlG85cD-xmF5vf9QwsmYJn8iHMKtzou756N854&usqp=CAU");
        }
        Company company = companyRepository.getById(id);
        Course course = courseRepository.getById(courseId);

        company.addGroup(group);
        group.setCompany(company);
        group.addCourse(course);
        course.addGroup(group);

        groupRepository.save(group);

    }

    @Override
    public void addGroup(Long id, Group group) {
        if (group.getImage().length()<15){
            group.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6ZKLU-ncmqfKAVjB03AcWFuB2mdVuYGlG85cD-xmF5vf9QwsmYJn8iHMKtzou756N854&usqp=CAU");
        }
        Company company = companyRepository.getById(id);
        company.addGroup(group);
        group.setCompany(company);
        groupRepository.save(group);
    }

    @Override
    public Group getGroupById(Long id) {
        return groupRepository.getById(id);
    }

    @Override
    public void updateGroup(Group group, Long id) {
        Group group1 = groupRepository.getById(id);
        group1.setGroupName(group.getGroupName());
        group1.setDateOfStart(group.getDateOfStart());
        group1.setImage(group.getImage());
        groupRepository.save(group1);
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = groupRepository.getById(id);
        for (Student s: group.getStudents()) {
            group.getCompany().minusStudent();
        }

        for (Course c: group.getCourses()) {
            for (Student student: group.getStudents()) {
                for (Instructor i: c.getInstructors()) {
                    i.minus();
                }
            }
        }

        for (Course c : group.getCourses()) {
            c.getGroups().remove(group);
            group.minusCount();
        }
        studentRepository.deleteAll(group.getStudents());
        group.setCourses(null);
        groupRepository.delete(group);
    }

    @Override
    public void assignGroup(Long courseId, Long groupId) throws IOException {
        Group group = groupRepository.getById(groupId);
        Course course = courseRepository.getById(courseId);
        if (course.getGroups()!=null){
            for (Group g : course.getGroups()) {
                if (g.getId() == groupId) {
                    throw new IOException("This group already exists!");
                }
            }
        }

        if (course.getInstructors() != null) {
            for (Instructor i: course.getInstructors()) {
                for (Student s: group.getStudents()) {
                    i.plus();
                }
            }
        }

        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        groupRepository.save(group);
    }
}
