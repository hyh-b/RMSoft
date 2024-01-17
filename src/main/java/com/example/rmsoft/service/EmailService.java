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

    // 비밀번호 재설정 메시지 전송
    public void sendPasswordResetMessage(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }
    // 비밀번호 재설정 매시지 이메일 컨텐츠
    public void sendPasswordResetEmail(String memberId, String email) throws MessagingException {
        String token = jwtTokenUtil.generateToken(memberId, email);

        String resetLink = "http://43.201.154.250:8080/resetPassword?token=" + token;

        String content = "<p>비밀번호 재설정을 위해 아래 링크를 클릭하세요(유효시간 1시간):</p>"
                + "<a href='" + resetLink + "'>비밀번호 재설정</a>";
        sendPasswordResetMessage(email, "비밀번호 재설정", content);
    }
}
