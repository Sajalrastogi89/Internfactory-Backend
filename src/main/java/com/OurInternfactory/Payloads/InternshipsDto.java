package com.OurInternfactory.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class InternshipsDto {
    private Integer id;
    @NotEmpty
    private String displayName;
    @NotEmpty
    private String provider;
    @NotEmpty
    private String title;
    @NotEmpty
    private String type;
    private String tenure;
    private String stipend;
    @JsonFormat(pattern="dd/MM/yyyy")
    @Future(message = "Enter valid date.")
    private Date lastDate;
    private String about;
    private String skills;
    private String who_can_apply;
    private String perks;
    private String imageUrl;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date issuedDate;
    private CategoryDTO category;
}
