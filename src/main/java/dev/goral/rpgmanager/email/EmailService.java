package dev.goral.rpgmanager.email;

import dev.goral.rpgmanager.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value( "${baseUrl}" )
    private String baseUrl;

    public void sendVerificationEmail(User user) {

        String subject = "Potwierdź swój adres e-mail";
        String confirmationUrl = baseUrl+"/api/v1/auth/activate?token=" + user.getToken();
        String message = "Kliknij w poniższy link, aby aktywować swoje konto: \n" + confirmationUrl;

        sendEmail(user.getEmail(), subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
    }
}

