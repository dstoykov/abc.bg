package dst.abc_bg.services;

import dst.abc_bg.entities.ReceiveEmail;
import dst.abc_bg.models.view.ReceiveEmailViewModel;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Set;

public interface ReceiveEmailService {
    Set<ReceiveEmail> receiveEmails() throws Exception;

    Set<ReceiveEmailViewModel> allNonDeletedReceivedMailsForUser(String username);
}
