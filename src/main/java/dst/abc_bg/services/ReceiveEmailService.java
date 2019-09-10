package dst.abc_bg.services;

import dst.abc_bg.entities.ReceiveEmail;
import dst.abc_bg.exceptions.CannotAccessMailException;
import dst.abc_bg.models.view.ReceiveEmailViewModel;

import java.util.Set;

public interface ReceiveEmailService {
    Set<ReceiveEmail> receiveEmails() throws Exception;

    Set<ReceiveEmailViewModel> allNonDeletedReceivedMailsForUser(String username);

    ReceiveEmailViewModel getNonDeletedReceivedEmailViewModelById(String id, String name) throws CannotAccessMailException;

    boolean deleteMail(String id, String name) throws CannotAccessMailException;

    Set<ReceiveEmailViewModel> allReceivedMails();

    ReceiveEmailViewModel getViewModelById(String id);
}
