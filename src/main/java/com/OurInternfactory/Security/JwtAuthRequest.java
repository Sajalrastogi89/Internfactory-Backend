package com.OurInternfactory.Security;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class JwtAuthRequest {
    @Email
    private String email;
    @Size(min = 8, message = "{\"Password\":\"Must be of minimum 8 characters\"}")
    private String password;
}

