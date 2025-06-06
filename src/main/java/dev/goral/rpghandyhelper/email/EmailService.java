package dev.goral.rpghandyhelper.email;

import dev.goral.rpghandyhelper.scheduler.entity.Scheduler;
import dev.goral.rpghandyhelper.scheduler.entity.SchedulerParticipant;
import dev.goral.rpghandyhelper.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${baseUrl}")
    private String baseUrl;

    public void sendVerificationEmail(User user) {
        String subject = "Aktywacja Konta | RPG Handy Helper";
        String confirmationUrl = baseUrl + "/activate?token=" + user.getToken();
        String htmlContent = generateActivationEmailTemplate(confirmationUrl);

        sendEmail(user.getEmail(), subject, htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String setFromMail = String.format("%s <%s>", "RPG Handy Helper Team", "rpg@adrian-goral.dev");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            message.setFrom(setFromMail);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    private String generateActivationEmailTemplate(String url) {
        return """
                <!DOCTYPE html>
                <html lang="pl">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Aktywacja Konta</title>
                <style>
                    body { background-color: #1e1e1e; color: #f3f4f4; font-family: Arial, sans-serif; text-align: center; padding: 20px; }
                    h1 { color: #12e1b9; }
                    p { font-size: 18px; line-height: 1.5; }
                    .button {
                        display: inline-block; background-color: #12e1b9; color: #1e1e1e;
                        padding: 10px 20px; border-radius: 4px; text-decoration: none;
                        font-size: 18px; font-weight: bold;
                    }
                </style>
                </head>
                <body>
                    <img src="https://github.com/xEdziu/RPG-Handy-Helper/raw/main/banner-rpg.png" alt="Logo" width="500">
                    <h1>AKTYWACJA KONTA</h1>
                    <p>Cześć! Witaj w grze. Dobrze mieć Cię tu z nami!</p>
                    <p>Naciśnij przycisk poniżej, by aktywować konto i zacząć przygodę!</p>
                    <p><a href="%s" class="button">AKTYWUJ</a></p>
                    <hr>
                    <p><strong>RPG Handy Helper Team</strong></p>
                </body>
                </html>
                """.formatted(url);
    }

    public void sendResetPasswordEmail(User userToUpdate) {
        String subject = "Resetowanie Hasła | RPG Handy Helper";
        String resetUrl = baseUrl + "/resetPassword?token=" + userToUpdate.getToken();
        String htmlContent = generateResetPasswordEmailTemplate(resetUrl);

        sendEmail(userToUpdate.getEmail(), subject, htmlContent);
    }

    private String generateResetPasswordEmailTemplate(String resetUrl) {

        return """
                <!DOCTYPE html>
                <html lang="pl">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Resetowanie Hasła</title>
                <style>
                    body { background-color: #1e1e1e; color: #f3f4f4; font-family: Arial, sans-serif; text-align: center; padding: 20px; }
                    h1 { color: #12e1b9; }
                    p { font-size: 18px; line-height: 1.5; }
                    .button {
                        display: inline-block; background-color: #12e1b9; color: #1e1e1e;
                        padding: 10px 20px; border-radius: 4px; text-decoration: none;
                        font-size: 18px; font-weight: bold;
                    }
                </style>
                </head>
                <body>
                    <img src="https://github.com/xEdziu/RPG-Handy-Helper/raw/main/banner-rpg.png" alt="Logo" width="500">
                    <h1>RESETOWANIE HASŁA</h1>
                    <p>Cześć!</p>
                    <p>Kości czasami są jak hasła, potrafią się zgubić... Na szczęście hasło można zresetować!</p>
                    <p>Naciśnij przycisk poniżej, by zresetować hasło.</p>
                    <p><a href="%s" class="button">RESETUJ</a></p>
                    <hr>
                    <p><strong>RPG Handy Helper Team</strong></p>
                </body>
                </html>
                """.formatted(resetUrl);
    }

    public void sendFinalDecisionNotification(Scheduler scheduler) {

        if (scheduler.getFinalDecision() == null) {
            throw new IllegalStateException("Nie ustanowiono jeszcze głównego terminu spotkania.");
        }

        for (SchedulerParticipant participant : scheduler.getParticipants()) {
            String email = participant.getPlayer().getEmail();
            if (email == null || email.isBlank()) continue;

            String subject = "Potwierdzono termin sesji | RPG Handy Helper";
            String htmlContent = generateFinalDecisionEmailTemplate(
                    participant.getPlayer().getUsername(),
                    scheduler.getTitle(),
                    scheduler.getFinalDecision().getStart(),
                    scheduler.getFinalDecision().getEnd(),
                    scheduler.getGoogleCalendarLink()
            );

            sendEmail(email, subject, htmlContent);
        }
    }

    private String generateFinalDecisionEmailTemplate(String username, String title, LocalDateTime start, LocalDateTime end, String calendarLink) {
        String formattedStart = start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String formattedEnd = end.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return """
            <!DOCTYPE html>
            <html lang="pl">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Termin Sesji</title>
                <style>
                    body { background-color: #1e1e1e; color: #f3f4f4; font-family: Arial, sans-serif; text-align: center; padding: 20px; }
                    h1 { color: #12e1b9; }
                    p { font-size: 18px; line-height: 1.5; }
                    .button {
                        display: inline-block; background-color: #12e1b9; color: #1e1e1e;
                        padding: 10px 20px; border-radius: 4px; text-decoration: none;
                        font-size: 18px; font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <img src="https://github.com/xEdziu/RPG-Handy-Helper/raw/main/banner-rpg.png" alt="Logo" width="500">
                <h1>TERMIN SESJI POTWIERDZONY</h1>
                <p>Cześć %s!</p>
                <p>Twoja sesja <strong>%s</strong> została zaplanowana:</p>
                <p><strong>%s – %s</strong></p>
                <p>Dodaj wydarzenie do swojego kalendarza:</p>
                <p><a href="%s" class="button">Dodaj do Kalendarza Google</a></p>
                <hr>
                <p><strong>RPG Handy Helper Team</strong></p>
            </body>
            </html>
            """.formatted(username, title, formattedStart, formattedEnd, calendarLink);
    }

    public void sendSchedulerCreatedNotification(Scheduler scheduler) {
        for (SchedulerParticipant participant : scheduler.getParticipants()) {
            String email = participant.getPlayer().getEmail();
            if (email == null || email.isBlank()) continue;

            String subject = "Nowy terminarz sesji RPG | RPG Handy Helper";

            String htmlContent = generateSchedulerCreatedEmailTemplate(
                    participant.getPlayer().getUsername(),
                    scheduler.getTitle(),
                    scheduler.getDeadline()
            );

            sendEmail(email, subject, htmlContent);
        }
    }

    private String generateSchedulerCreatedEmailTemplate(String username, String title, LocalDateTime deadline) {
        String formattedDeadline = deadline.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        return """
            <!DOCTYPE html>
            <html lang="pl">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Nowy Terminarz</title>
                <style>
                    body { background-color: #1e1e1e; color: #f3f4f4; font-family: Arial, sans-serif; text-align: center; padding: 20px; }
                    h1 { color: #12e1b9; }
                    p { font-size: 18px; line-height: 1.5; }
                </style>
            </head>
            <body>
                <img src="https://github.com/xEdziu/RPG-Handy-Helper/raw/main/banner-rpg.png" alt="Logo" width="500">
                <h1>NOWY TERMINARZ SESJI</h1>
                <p>Cześć %s!</p>
                <p>GameMaster właśnie utworzył nowy terminarz sesji: <strong>%s</strong>.</p>
                <p>Prosimy, uzupełnij swoją dostępność do <strong>%s</strong>.</p>
                <hr>
                <p><strong>RPG Handy Helper Team</strong></p>
            </body>
            </html>
            """.formatted(username, title, formattedDeadline);
    }




}
