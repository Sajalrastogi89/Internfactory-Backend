package com.OurInternfactory.Payloads;
import lombok.Getter;
import lombok.Setter;
@Getter@Setter
public class OtpDto {
    private String email;
    private int one_time_password;
}