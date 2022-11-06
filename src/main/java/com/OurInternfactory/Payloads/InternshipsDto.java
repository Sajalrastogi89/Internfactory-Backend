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
    private String about;
    private Date issuedDate;
    private Date lastDate;
    private String tenure;
    private String stipend;
    private String imageUrl;
    private CategoryDTO category;
}
