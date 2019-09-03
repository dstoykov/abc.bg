package dst.abc_bg.util;

import org.springframework.stereotype.Component;

import javax.mail.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@Component
public class EmailReceiver {
    private static final String ACCOUNT_NAME = "testov12r@gmail.com";
    private static final String PASSWORD = "Pesho523";
    private static final String HOST = "pop.gmail.com";
    private static final String PORT = "995";
    private static final String TRUE = "true";
    private static final String INBOX_FOLDER = "INBOX";
    private static final String POP3_SECURED_PROTOCOL = "pop3s";

    private static final int ZERO_INDEX = 0;

    public Message[] receiveEmail() throws MessagingException {
        Properties properties = configProperties();

        Session session = configSession(properties);

        Store store = configStore(session);

        Folder folder = store.getFolder(INBOX_FOLDER);
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.getMessages();

        Arrays.stream(messages).forEach(m -> {
            try {
                System.out.println(m.getSubject());
                System.out.println(m.getFrom()[ZERO_INDEX].toString());
                System.out.println(m.getContent().toString());
                System.out.println(m.getContentType());
                System.out.println();
            } catch (IOException | MessagingException e) {
                e.printStackTrace();
            }
        });

        folder.close(false);
        store.close();

        return messages;
    }

    private Store configStore(Session session) throws MessagingException {
        Store store = session.getStore(POP3_SECURED_PROTOCOL);
        store.connect(HOST, ACCOUNT_NAME, PASSWORD);

        return store;
    }

    private Session configSession(Properties properties) {
        return Session.getDefaultInstance(properties);
    }

    private Properties configProperties() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", HOST);
        properties.put("mail.pop3.port", PORT);
        properties.put("mail.pop3.starttls.enable", TRUE);
        return properties;
    }
}