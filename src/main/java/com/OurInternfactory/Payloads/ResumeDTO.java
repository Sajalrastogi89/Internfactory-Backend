package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {
    Integer id;
    private String fullName;
    private String education;
    private String internships;
    private String trainingCourses;
    private String projects;
    private String skills;
    private String  portfolio;
}
