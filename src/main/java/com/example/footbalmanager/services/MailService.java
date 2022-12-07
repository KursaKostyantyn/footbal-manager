package com.example.footbalmanager.services;

import com.example.footbalmanager.models.CustomUser;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;

    public void sendMail(CustomUser customUser){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("forjava2022@gmail.com");
            helper.setTo(customUser.getEmail());
            helper.setText("to activate account visit <a href='http://localhost:3000/activate?id="+customUser.getId()+"'>this</a> link",true);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);

    }
}
