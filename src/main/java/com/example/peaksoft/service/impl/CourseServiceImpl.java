package com.example.peaksoft.service.impl;

import com.example.peaksoft.model.Company;
import com.example.peaksoft.model.Course;
import com.example.peaksoft.repository.CompanyRepository;
import com.example.peaksoft.repository.CourseRepository;
import com.example.peaksoft.service.CourseService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CompanyRepository companyRepository) {
        this.courseRepository = courseRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Course> getAllCourses(Long id) {
        return  courseRepository.getAllCourses(id);
    }

    @Override
    public void addCourse(Long id, Course course) throws IOException {
        validator(course.getCourseName().replace(" ", ""), course.getDescription().replace(" ", ""), course.getDuration());
        Company company = companyRepository.getById(id);
        company.addCourse(course);
        course.setCompany(company);
        courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.getById(id);
    }

    @Override
    public void updateCourse(Course course, Long id) throws IOException {
        validator(course.getCourseName(), course.getDescription(), course.getDuration());
        Course course1 = courseRepository.getById(id);
        course1.setCourseName(course.getCourseName());
        course1.setDuration(course.getDuration());
        course1.setDescription(course.getDescription());
        courseRepository.save(course1);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.getById(id);
        courseRepository.delete(course);
    }


    private void validator(String courseName, String description, int duration) throws IOException {
        if (courseName.length()>3 && description.length()>5 && description.length()<15 && duration>0 && duration<24){
            for (Character i: courseName.toCharArray()) {
                if (!Character.isLetter(i)){
                    throw new IOException("В название курса нельзя вставлять цифры");
                }
            }
        }else {
            throw new IOException("Form error course registration");
        }
    }
}
