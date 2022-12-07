package com.example.peaksoft.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;


@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "company_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100000, name = "company_name")
    private String companyName;

    @Column(length = 100000, name = "located_country")
    private String locatedCountry;

    private int count;

    public void plusStudent(){
        count++;
    }
    public void minusStudent(){
        count--;
    }

    @OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "company")
    private List<Course> courses;
    public void addCourse(Course course1){
        if (courses==null){
            courses=new ArrayList<>();
        }
        courses.add(course1);
    }

    @OneToMany(cascade = {ALL}, fetch = LAZY, mappedBy = "company")
    private List<Group> groups;
    public void addGroup(Group group){
        if (groups==null){
            groups=new ArrayList<>();
        }
        groups.add(group);
    }

}
