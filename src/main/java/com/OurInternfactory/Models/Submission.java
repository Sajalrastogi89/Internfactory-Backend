package com.OurInternfactory.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "why_should_we_hire_you", length = 10000)
    private String why_should_we_hire_you;
    @Column(name = "share_your_work", length = 10000)
    private String share_your_work;
    @Column(name = "worked_in_team", length = 10000)
    private String worked_in_team;
    @Column(name = "strengths", length = 10000)
    private String strengths;
    @Column(name = "weakness", length = 10000)
    private String weakness;
    @Column(name = "hobbies", length = 10000)
    private String hobbies;
    @ManyToOne
    private User user;
    @ManyToOne
    private Internships internships;
}
