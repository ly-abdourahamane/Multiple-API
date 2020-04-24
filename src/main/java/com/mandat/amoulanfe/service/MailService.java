package com.mandat.amoulanfe.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Setter
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String url,String token ,String email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Confirmer votre inscription à mon application sans nom lol");
        helper.setTo(email);
        String urlVerification = url+token;
        helper.setText("\n" + "<html><body><h1>Email de confirmation</h1></body></html>\n" + urlVerification , true);
        mailSender.send(message);
    }

}