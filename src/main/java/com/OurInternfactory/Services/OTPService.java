package com.OurInternfactory.Services;
import org.springframework.stereotype.Service;
import java.util.Random;
@Service
public class OTPService {
    private final EmailService emailService;
    public OTPService(EmailService emailService) {
        this.emailService = emailService;
    }
    public int OTPRequest(String email){
        Random rand = new Random();
        int otpCheck = rand.nextInt(899999) +100000;
        String subject = "OTP Verification";
        String message = "Dear User," +
                "\nThe One Time Password (OTP) to verify your Email Address is " + otpCheck +
                "\nThe One Time Password is valid for the next 10 minutes."+
                "\n(This is an auto generated email, so please do not reply back.)" +
                "\nRegards," +
                "\nTeam INTERNFACTORY";
        String to = email;
        this.emailService.sendEmail(subject, message, to);
        return otpCheck;
    }
}