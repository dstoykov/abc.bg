package dst.abc_bg.areas.email.send.util;

import dst.abc_bg.areas.email.send.models.service.SendEmailServiceModel;
import dst.abc_bg.areas.email.util.EmailConstraints;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    public boolean sendEmail(SendEmailServiceModel emailServiceModel) throws MessagingException {
        Properties properties = configProperties();
        Session session = configSession(properties);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailConstraints.ACCOUNT_NAME));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailServiceModel.getRecipient()));
        message.setSubject(emailServiceModel.getSubject());
        message.setText(emailServiceModel.getContent());

        Transport.send(message);

        return true;
    }

    private Properties configProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", EmailConstraints.TRUE);
        properties.put("mail.smtp.starttls.enable", EmailConstraints.TRUE);

        return properties;
    }

    private Session configSession(Properties properties) {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConstraints.ACCOUNT_NAME, EmailConstraints.PASSWORD);
            }
        });
        session.setDebug(true);

        return session;
    }
}
