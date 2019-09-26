package dst.abc_bg.areas.email.receive.util;

import dst.abc_bg.areas.email.util.EmailConstraints;
import org.springframework.stereotype.Component;

import javax.mail.*;
import java.io.IOException;
import java.util.*;

@Component
public class EmailReceiver {
    private static final String HOST = "pop.gmail.com";
    private static final String PORT = "995";
    private static final String INBOX_FOLDER = "INBOX";
    private static final String POP3_SECURED_PROTOCOL = "pop3s";

    public Set<Message> receiveEmail() throws MessagingException {
        Properties properties = configProperties();

        Session session = configSession(properties);

        Store store = configStore(session);

        Folder folder = store.getFolder(INBOX_FOLDER);
        folder.open(Folder.READ_ONLY);

        Set<Message> messages = getMessagesOrdered(folder);

        messages.forEach(m -> {
            try {
                System.out.println(m.getSubject());
                System.out.println(m.getFrom()[EmailConstraints.ZERO_INDEX].toString());
                System.out.println(m.getContent().toString());
                System.out.println(m.getContentType());
                System.out.println(m.getSentDate().toString());
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
        store.connect(HOST, EmailConstraints.ACCOUNT_NAME, EmailConstraints.PASSWORD);

        return store;
    }

    private Session configSession(Properties properties) {
        return Session.getDefaultInstance(properties);
    }

    private Properties configProperties() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", HOST);
        properties.put("mail.pop3.port", PORT);
        properties.put("mail.pop3.starttls.enable", EmailConstraints.TRUE);
        return properties;
    }

    private Set<Message> getMessagesOrdered(Folder folder) throws MessagingException {
        Message[] messages = folder.getMessages();
        Set<Message> messagesOrdered = new TreeSet<>((o1, o2) -> {
            try {
                return o2.getSentDate().compareTo(o1.getSentDate());
            } catch (MessagingException e) {
                e.printStackTrace();
                return 0;
            }
        });

        Collections.addAll(messagesOrdered, messages);

        return messagesOrdered;
    }
}
