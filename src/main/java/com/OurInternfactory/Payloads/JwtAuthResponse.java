package com.OurInternfactory.Payloads;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    private String firstname;
    private String lastname;
    private String role;
}
