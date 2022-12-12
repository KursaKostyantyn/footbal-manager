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


    private void sendMail(CustomUser customUser, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("forjava2022@gmail.com");
            helper.setTo(customUser.getEmail());
            helper.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendMailForActivateUser(CustomUser customUser) {
        String text = "to activate account visit <a href='http://localhost:3000/activate?id=" + customUser.getId() + "'>this</a> link";
        sendMail(customUser, text);

    }

    public void sendMailForResetPassword(CustomUser customUser, String password) {
        String text = "to reset password visit <a href='http://localhost:3000/createNewPassword?resetPassword=" + password + "'>this</a> link";
        sendMail(customUser, text);
    }

}
