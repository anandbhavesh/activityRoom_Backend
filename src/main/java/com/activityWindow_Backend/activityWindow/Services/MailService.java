package com.activityWindow_Backend.activityWindow.Services;


import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;





@Service
class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void send(String Recepeint, String token) {
        String Message = "Hi, Thanks for signing up. To complete the registration pls click the link below to activate your account...  http://localhost:8080/api/auth/accountVerification/" + token;
        String subject = "Account Activation ";
        String to = Recepeint;
        String from = "anandbhavesh037@gmail.com";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setText(Message);
        message.setSubject(subject);

        mailSender.send(message);


    }

    @Async
    public void send(String Recepeint, User user, Post post) {
        String Message = "Hi, " + user.getUsername() + " has texted you on GEchatThread pls click the link below to find more details on the Activity_" + post.getPostName();
        String subject = " " + user.getUsername() + "Texted you on GEchatThread";
        String to = Recepeint;
        String from = "anandbhavesh037@gmail.com";
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setText(Message);
        message.setSubject(subject);

        mailSender.send(message);


    }

}