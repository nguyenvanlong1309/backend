package com.tuthien.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    @Async
    public void sendMailAsync(String to, String content, String title) {
        this.sendMail(to, content, title);
    }

    public void sendMail(String to, String content, String title) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(content);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(simpleMailMessage);

    }

}
