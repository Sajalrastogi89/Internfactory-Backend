package com.OurInternfactory.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Internships {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(length=150, nullable = false)
    private String displayName;
    @Column(length=150, nullable = false)
    private String provider;
    @Column(length=150, nullable = false)
    private String title;
    @Column(length=150, nullable = false)
    private String type;
    private String tenure;
    private String stipend;
    private Date lastDate;
    @Column(length = 10000)
    private String about;
    @Column(length = 1000)
    private String skills;
    @Column(length = 1000)
    private String who_can_apply;
    @Column(length = 1000)
    private String perks;
    private String imageUrl;
    private Date issuedDate;
    private boolean active;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
//ProviderHOST
    @ManyToOne(cascade = CascadeType.ALL)
    public User userProvider;
//UserAPPLICATION
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<User> user;
    @OneToOne(cascade = CascadeType.ALL)
    public Submission submissionModel;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Submission> submissions;
}
