package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditResumeDto {
    @Email(message = "Email Address is not Valid!!")
    private String email;
    private ResumeDTO resumeDTO;
}
