package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileResponse {
    private Integer id;
    private String profilePhoto;
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String phoneNumber;
}
