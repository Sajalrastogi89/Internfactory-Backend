package com.OurInternfactory.Payloads;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    private String email;
    @NotEmpty
    private String firstname;
    private String lastname;
    private String gender;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be 10 digit long")
    private String phoneNumber;
}