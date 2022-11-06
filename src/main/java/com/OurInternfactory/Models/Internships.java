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
    @Column(name = "about", length = 10000)
    private String about;
    private String imageUrl;
    private Date issuedDate;
    private Date lastDate;
    private String tenure;
    private String stipend;
    @JsonIgnore
    @ManyToOne
    private Category category;
    @ManyToMany
    public Set<User> user;
}
