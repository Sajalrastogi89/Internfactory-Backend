package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApppliedUserDto{
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String phoneNumber;
    private String profilePhoto;
    private List<SubmissionDto> submission;
    private ResumeDTO resume;
}