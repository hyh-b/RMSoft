package com.example.rmsoft.service;

import com.example.rmsoft.jwtToken.JwtTokenUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void sendPasswordResetMessage(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(String memberId, String email) throws MessagingException {
        String token = jwtTokenUtil.generateToken(memberId, email);

        String resetLink = "http://localhost:8080/resetPassword?token=" + token;

        String content = "<p>비밀번호 재설정을 위해 아래 링크를 클릭하세요(유효시간 1시간):</p>"
                + "<a href='" + resetLink + "'>비밀번호 재설정</a>";
        sendPasswordResetMessage(email, "비밀번호 재설정", content);
    }
}
