package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class EditUserDto {
    @Email(message = "Email Address is not Valid!!")
    private String email;
    @NotEmpty
    private String firstname;
    private String lastname;
    private String gender;
    @NotEmpty
    @Email(message = "Email Address is not Valid!!")
    private String newemail;
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phoneNumber;
}
