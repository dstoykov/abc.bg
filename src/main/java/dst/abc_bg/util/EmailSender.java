package dst.abc_bg.util;

import dst.abc_bg.models.service.SendEmailServiceModel;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String ACCOUNT_NAME = "testov12r@gmail.com";
    private static final String PASSWORD = "Pesho523";
    private static final String SMTP_PORT = "587";
    private static final String TRUE = "true";

    public boolean sendEmail(SendEmailServiceModel emailServiceModel) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", TRUE);
        properties.put("mail.smtp.starttls.enable", TRUE);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ACCOUNT_NAME, PASSWORD);
            }
        });
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(ACCOUNT_NAME));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailServiceModel.getRecipient()));
        message.setSubject(emailServiceModel.getSubject());
        message.setText(emailServiceModel.getContent());

        Transport.send(message);

        return true;
    }
}
