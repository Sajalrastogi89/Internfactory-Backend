package com.OurInternfactory.Services;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String subject, String message, String to){
        //rest of the code...

        boolean f = false;

        String from = "teaminternfactory@gmail.com";
        //Variable for gmail
        String host = "smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES "+properties);

        //setting important information to properties object

        //host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        //Step1: to get the session Object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("teaminternfactory@gmail.com", "chpfgyxduagwtenb");
            }
        });
         session.setDebug(true);

         MimeMessage m  =new MimeMessage(session);

         try{
             //from email
             m.setFrom(from);

             //adding recipient to message
             m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

             //adding subject to message
             m.setSubject(subject);

             //adding text to message
             m.setText(message);

             //send

             //Step 3: send the message using Transport Class
             Transport.send(m);

             System.out.println("Sent success.................");
             f =true;
         }
         catch (Exception e){
             e.printStackTrace();
         }
        return f;
    }
}
