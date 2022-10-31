package com.OurInternfactory.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter@Setter
public class ForgetEmail {
    @NotEmpty
    @Email(message = "Email Address is not Valid!!")
    String email;
}
