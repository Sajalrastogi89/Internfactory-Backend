package com.OurInternfactory.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;
    @Column(name = "fullName", length = 100)
    private String fullName;
    @Column(name = "education", length = 10000)
    private String education;
    @Column(name = "internships", length = 10000)
    private String internships;
    @Column(name = "trainingCourses", length = 10000)
    private String trainingCourses;
    @Column(name = "projects", length = 10000)
    private String projects;
    @Column(name = "skills", length = 10000)
    private String skills;
    @Column(name = "portfolio", length = 10000)
    private String  portfolio;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private User user;
}
