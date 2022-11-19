package com.OurInternfactory.Services;
import com.OurInternfactory.Payloads.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;
@Service
public class OTPService {
    private final EmailService emailService;
    private final SmsSender smsSender;
    public OTPService(EmailService emailService, SmsSender smsSender) {
        this.emailService = emailService;
        this.smsSender = smsSender;
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

    public void sendSMS(SmsRequest smsRequest){
        this.smsSender.sendSms(smsRequest);
    }

    public int OTPRequestMobile(String phoneNumber){
        Random rand = new Random();
        int otpCheck = rand.nextInt(899999) +100000;


        String subject = "OTP Verification";
        String message = "Dear User," +
                "\nThe One Time Password (OTP) to verify your Email Address is " + otpCheck +
                "\nThe One Time Password is valid for the next 10 minutes."+
                "\n(This is an auto generated email, so please do not reply back.)" +
                "\nRegards," +
                "\nTeam INTERNFACTORY";
        String to = phoneNumber;
        if(!phoneNumber.contains("+91")) to = "+91"+phoneNumber;
        SmsRequest smsRequest = new SmsRequest(to, message);
        sendSMS(smsRequest);
//        this.emailService.sendEmail(subject, message, to);
        return otpCheck;
    }
}