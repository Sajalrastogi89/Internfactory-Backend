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
    @Column(length = 100)
    private String fullName;
    @Column(length = 10000)
    private String education;
    @Column(length = 10000)
    private String internships;
    @Column(length = 10000)
    private String trainingCourses;
    @Column(length = 10000)
    private String projects;
    @Column(length = 10000)
    private String skills;
    @Column(length = 10000)
    private String  portfolio;
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;
}
