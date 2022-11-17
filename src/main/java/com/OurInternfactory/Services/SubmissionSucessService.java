package com.OurInternfactory.Services;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SubmissionSucessService {
    private final EmailService emailService;
    public SubmissionSucessService(EmailService emailService) {
        this.emailService = emailService;
    }
    public void applicationMessage(String email, String internshipName){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
        String subject = "Application Successfully Submitted";
        String message = "Dear User," +
                "\nYou have successfully submitted the application, " + internshipName + " on "+ formatter.format(date) +
                ".\n(This is an auto generated email, so please do not reply back.)" +
                "\nRegards," +
                "\nTeam INTERNFACTORY";
        String to = email;
        this.emailService.sendEmail(subject, message, to);
    }
}