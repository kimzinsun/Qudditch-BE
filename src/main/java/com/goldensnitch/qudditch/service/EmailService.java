package com.goldensnitch.qudditch.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/* @Slf4j
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
} */
@Slf4j
@Service
public class EmailService {
    // Spring Boot에 의해 자동 생성된 SendGrid 빈을 주입받습니다.
    private final SendGrid sendGrid;
    
    // application.properties에서 발신자 이메일을 읽어옵니다.
    private final String fromEmail;

    // 생성자 주입을 위해 @Autowired 주석을 제거
    public EmailService(SendGrid sendGrid,
                        @Value("${twilio.sendgrid.from-email}") String fromEmail) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    // 단일 이메일을 전송하는 메소드입니다.
    public void sendSingleEmail(String toEmail) {
        Email from = new Email(this.fromEmail);
        String subject = "Hello, World!";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "SendGrid를 이용한 이메일 전송 예제입니다.");

        Mail mail = new Mail(from, subject, to, content);

        sendEmail(mail);
    }

    // 이메일 전송 실패 시 사용자 친화적인 메시지 반환 메소드
    
    private void sendEmail(Mail mail) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
    
            Response response = sendGrid.api(request);
            if (response.getStatusCode() != HttpStatus.OK.value()) {
                log.error("Email sending failed: {}", response.getBody());
                throw new EmailSendingException("Failed to send email.");
            }
        } catch (IOException e) {
            log.error("Error sending email", e);
            throw new EmailSendingException("An error occurred while sending the email.");
        }
    }

    public void sendVerificationEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendVerificationEmail'");
    }
}