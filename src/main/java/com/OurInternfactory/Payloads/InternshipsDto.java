package com.OurInternfactory.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.util.Date;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class InternshipsDto {
    private Integer id;
    @NotEmpty
    private String title;
    private String type;
    private String tenure;
    private String stipend;
    @JsonFormat(pattern="yyyy/MM/dd")
    @Future(message = "Enter valid date.")
    private Date lastDate;
    private String about;
    private String skills;
    private String who_can_apply;
    private String perks;
    private String imageUrl;
    @JsonFormat(pattern="yyyy/MM/dd")
    private Date issuedDate;
    private CategoryDTO category;
}
