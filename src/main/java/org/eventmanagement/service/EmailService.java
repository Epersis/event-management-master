package org.eventmanagement.service;


import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;

import org.eventmanagement.dto.EmailDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    @Async
    public void sendEmail(EmailDetailsDto emailDetailsDto) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDetailsDto.getTo());
            helper.setSubject(emailDetailsDto.getSubject());

            Template template = freemarkerConfig.getTemplate(emailDetailsDto.getTemplateName());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, emailDetailsDto.getModel());

            helper.setText(html, true);

            emailSender.send(message);
        } catch (Exception ex) {
            LOGGER.error("Failed to send the mail", ex);
        }
    }
}
