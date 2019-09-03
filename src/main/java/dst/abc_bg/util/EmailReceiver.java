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

    public Message[] receiveEmail() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", HOST);
        properties.put("mail.pop3.port", PORT);
        properties.put("mail.pop3.starttls.enable", TRUE);

        Session session = Session.getDefaultInstance(properties);

        Store store = session.getStore("pop3s");
        store.connect(HOST, ACCOUNT_NAME, PASSWORD);

        Folder folder = store.getFolder(INBOX_FOLDER);
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.getMessages();

        Arrays.stream(messages).forEach(m -> {
            try {
                System.out.println(m.getSubject());
                System.out.println(m.getFrom()[0].toString());
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
}
