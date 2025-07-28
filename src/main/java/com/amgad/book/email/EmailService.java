package com.amgad.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    //something to be added
    @Async
    public void sendEmail(EmailData emailData) throws MessagingException {
        log.info("Sending email : {}", emailData);
        String templateName;
        if (emailData.getEmailTemplateName() == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailData.getEmailTemplateName().getName();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
        Map<String, Object>properties=new HashMap<>();
        properties.put("username", emailData.getUsername());
        properties.put("confirmationUrl", emailData.getConfirmationUrl());
        properties.put("activationCode", emailData.getActivationCode());
        Context context = new Context();
        context.setVariables(properties);
        mimeMessageHelper.setFrom(emailData.getFrom());
        mimeMessageHelper.setTo(emailData.getTo());
        mimeMessageHelper.setSubject(emailData.getSubject());
        String template = templateEngine.process(templateName, context);
        mimeMessageHelper.setText(template, true);
        mailSender.send(mimeMessage);
        log.info("Email sent : {}", emailData);
    }
}
