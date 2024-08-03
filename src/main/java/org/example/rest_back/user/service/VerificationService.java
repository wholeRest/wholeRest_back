package org.example.rest_back.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "wholerest.site";
    private static String number;

    public VerificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // 인증 코드 생성 (6자리)
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // 0-9까지의 숫자 중 하나를 추가
        }
        return code.toString();
    }

    // 이메일 작성 메서드
    private MimeMessage createMail(String recipientEmail) {
        number = generateCode();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("온쉼표_wholeRest : 본인 확인 코드가 전송되었습니다.");

            // HTML 본문 구성
            String body = "<html><body style='font-family: Arial, sans-serif; color: #333; margin: 0; padding: 0;'>";
            body += "<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f4; border-radius: 8px; border: 1px solid #ddd;'>";
            body += "<div style='text-align: center; padding: 10px; background-color: #FFE14F; border-radius: 8px 8px 0 0;'>";
            body += "<h1 style='margin: 0; color: #333;'>온쉼표(wholeRest)</h1>";
            body += "<p style='margin: 0; color: #333;'>인증번호를 확인하세요 !</p>";
            body += "</div>";
            body += "<div style='padding: 20px;'>";
            body += "<h3 style='color: #4CAF50; text-align: center;'>인증 번호를 입력해주세요.</h3>";
            body += "<h1 style='font-size: 36px; color: #FF5722; text-align: center;'>" + number + "</h1>";
            body += "<div style='border-top: 1px solid #ddd; margin-top: 20px; padding-top: 20px;'>";
            body += "<p style='text-align: center; color: #555;'>우린 누구나 장애를 가지고 있습니다.</p>";
            body += "<p style='text-align: center; color: #555;'>야스토미 아유미의 책 \"<strong>단단한 삶</strong>\"을 보면 형태가 다를 뿐, 누구나 장애를 가지고 있다고 합니다.</p>";
            body += "<p style='text-align: center; color: #555;'>선한 영향력을 온쉼표를 통해 나누고, 모두가 더 나은 삶을 지향했으면 합니다.</p>";
            body += "</div>";
            body += "</div></body></html>";
            helper.setText(body, true); // HTML 형식의 본문 사용
        } catch (MessagingException e) {
            e.printStackTrace(); // 예외 처리
        }

        return message;
    }

    // 이메일 전송 메서드
    public String sendEmail(String recipientEmail) throws MessagingException {
        MimeMessage message = createMail(recipientEmail);
        javaMailSender.send(message);
        return number; // 인증 번호 반환
    }
}

