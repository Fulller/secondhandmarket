package com.secondhandmarket.service;

import com.secondhandmarket.dto.email.SendEmailDto;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Report;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {
//    @Value("${spring.mail.username}")
//    private String systemEmail;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String systemEmail;

    public void sendEmail(SendEmailDto emailPayload) {
        var message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailPayload.getTo());
            helper.setSubject(emailPayload.getSubject());
            helper.setText(emailPayload.getText(), true);
            helper.setFrom(systemEmail);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Failed to send email", "mail-e-01");
        }
    }

    public void sendEmailToVerifyRegister(String toEmail, String verificationCode) {
        String verifyUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/auth/register/verify/{verificationCode}")
                .buildAndExpand(verificationCode)
                .toUriString();
        String content = loadEmailTemplate("templates/mail/register.html");
        String emailText = "Hãy nhấn vào nút bên dưới để xác nhận và hoàn tất quá trình đăng ký!";
        content = content.replace("{{verifyUrl}}", verifyUrl) // Đúng cú pháp
                            .replace("{{emailText}}", emailText);
        System.out.println("aaa"+  content);
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("MAIL XÁC NHẬN ĐĂNG KÝ")
                .text(content)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailToWelcome(String toEmail) {
        String emailText = "Chào mừng bạn đến với Chợ Cũ!";
        String content = loadEmailTemplate("templates/mail/welcome.html");
        content = content.replace("{{emailText}}", emailText);
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("CHÀO MỪNG BẠN ĐẾN VỚI CHỢ CŨ")
                .text(content)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailToVerifyForgotPassword(String toEmail, String verificationCode) {
        String content = loadEmailTemplate("templates/mail/forget-password.html");
        content = content.replace("{{emailCode}}", verificationCode);
        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("KHÔI PHỤC MẬT KHẨU")
                .text(content)
                .build();
        sendEmail(emailPayload);
    }

    public void sendEmailReport(String toEmail, Report report) {
        String content = loadEmailTemplate("templates/mail/report.html");
        content = content.replace("{{defendant}}", report.getDefendant().getName()).
                          replace("{{accused}}", report.getAccused().getName()).
                          replace("{{reportedAt}}", String.valueOf(report.getReportedAt())).
                          replace("{{reason}}", report.getReason());

        SendEmailDto emailPayload = SendEmailDto.builder()
                .to(toEmail)
                .subject("MAIL BÁO CÁO")
                .text(content)
                .build();
        sendEmail(emailPayload);
    }

    private String loadEmailTemplate(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            byte[] fileData = StreamUtils.copyToByteArray(resource.getInputStream());
            return new String(fileData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
