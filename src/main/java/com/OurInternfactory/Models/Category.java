package com.OurInternfactory.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Categories1")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int categoryId;
    @Column(name = "CateName", nullable = false)
    private String categoryName;
    @Column(name = "imageName")
    private String imageName;
    @Column(name = "count")
    private int count;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Internships> internshipsList = new ArrayList<>();
}