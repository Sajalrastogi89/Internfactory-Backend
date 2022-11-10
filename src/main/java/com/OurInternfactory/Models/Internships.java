package com.OurInternfactory.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "internships")
@Getter @Setter
@NoArgsConstructor
public class Internships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", length=150, nullable = false)
    private String title;
    @Column(name = "type", length=150, nullable = false)
    private String type;
    private String tenure;
    private String stipend;
    private Date lastDate;
    @Column(name = "about", length = 10000)
    private String about;
    @Column(name = "skills", length = 1000)
    private String skills;
    @Column(name = "who_can_apply", length = 1000)
    private String who_can_apply;
    @Column(name = "perks", length = 1000)
    private String perks;

    private String imageUrl;
    private Date issuedDate;
    @JsonIgnore
    @ManyToOne
    private Category category;
    @JsonIgnore
    @ManyToMany
    public Set<User> user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Submission> submissions;
}
