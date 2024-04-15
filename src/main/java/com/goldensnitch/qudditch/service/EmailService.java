package com.goldensnitch.qudditch.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.extern.slf4j.Slf4j;



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
    private final SendGrid sendGrid;
    private final String fromEmail;

    @Autowired
    public EmailService(@Value("${spring.sendgrid.api-key}") String apiKey,
                        @Value("${twilio.sendgrid.from-email}") String fromEmail) {
        this.sendGrid = new SendGrid(apiKey);
        this.fromEmail = fromEmail;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) throws IOException {
        String subject = "계정 인증 코드";
        String contentText = "귀하의 인증 코드는 다음과 같습니다: " + verificationCode;

        Email from = new Email(this.fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        

        // 이메일 구성 및 전송 로직
        try {
            sendEmail(mail);
        } catch (IOException e) {
            log.error("이메일 인증 보내기에 실패하였습니다.", e);
            throw new EmailSendingException("이메일 보내기에 실패했습니다: " + e.getMessage());
        }
    }
    

    public void sendPasswordResetEmail(String toEmail, String temporaryPassword) throws IOException {
        String subject = "비밀번호 재설정 요청";
        String contentText = "임시 비밀번호: " + temporaryPassword +
                            "\n로그인 후 비밀번호를 변경해 주세요.";
        
        Email from = new Email(this.fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        sendEmail(mail); // 메일 전송
    }

    // 메일을 실제로 전송하는 메서드
    private void sendEmail(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        int statusCode = response.getStatusCode();
        // 202를 성공으로 처리
        if (statusCode == HttpStatus.ACCEPTED.value() || statusCode == HttpStatus.OK.value()) {
            log.info("성공적으로 이메일을 보냈습니다.: {}", response.getBody());
        } else {
            log.error("이메일 보내기에 실패하였습니다.: {}", response.getBody());
            throw new EmailSendingException("이메일 보내기에 실패하였습니다. Status code: " + response.getStatusCode());
        }
    }

    // 기타 메서드...
}