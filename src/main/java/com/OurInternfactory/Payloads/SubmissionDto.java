package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {
    private Integer id;
    private List<Question> questions;
//    private String why_should_we_hire_you;
//    private String share_your_work;
//    private String worked_in_team;
//    private String strengths;
//    private String weakness;
//    private String hobbies;
    private InternshipsDto internships;

}
