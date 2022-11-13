package com.OurInternfactory.Payloads;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

@Getter@Setter
public class OtpDto {
    @Email
    private String email;
    //    @DecimalMax()
    @Min(value=100000, message="OTP should be 6 digit number")
    @Digits(message="OTP should be 6 digit number", fraction = 0, integer = 6)
    private int one_time_password;
}