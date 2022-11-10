package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class InternshipsDto {
    private Integer id;
    private String title;
    private String type;
    private String tenure;
    private String stipend;
    private Date lastDate;
    private String about;
    private String skills;
    private String who_can_apply;
    private String perks;
    private String imageUrl;
    private Date issuedDate;
    private CategoryDTO category;
}
