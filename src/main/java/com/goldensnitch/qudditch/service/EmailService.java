package com.goldensnitch.qudditch.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class EmailService {
    private final SendGrid sendGrid;
    private final String fromEmail;

    public EmailService(
            // get the SendGrid bean automatically created by Spring Boot
            @Autowired SendGrid sendGrid,
            // read your email to use as sender from application.properties
            @Value("${twilio.sendgrid.from-email}") String fromEmail
    ) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    public void sendSingleEmail(String toEmail) {
        Email from = new Email(this.fromEmail);
        String subject = "Hello, World!";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Welcome to the Twilio SendGrid world!");

        Mail mail = new Mail(from, subject, to, content);

        sendEmail(mail);
    }

    private void sendEmail(Mail mail) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();
            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException(response.getBody());
            }
        } catch (IOException e) {
            log.error("Error sending email", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}