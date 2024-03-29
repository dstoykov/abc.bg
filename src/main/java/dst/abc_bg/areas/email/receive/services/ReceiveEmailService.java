package dst.abc_bg.areas.email.receive.services;

import dst.abc_bg.areas.email.exceptions.CannotAccessMailException;
import dst.abc_bg.areas.email.receive.models.service.ReceiveEmailServiceModel;
import dst.abc_bg.areas.email.receive.models.view.ReceiveEmailViewModel;

import java.util.Set;

public interface ReceiveEmailService {
    Set<ReceiveEmailServiceModel> receiveEmails() throws Exception;

    Set<ReceiveEmailViewModel> getAllNonDeletedReceivedMailsForUser(String username);

    ReceiveEmailViewModel getNonDeletedReceivedEmailViewModelById(String id, String name) throws CannotAccessMailException;

    Boolean deleteMail(String id, String name) throws CannotAccessMailException;

    Set<ReceiveEmailViewModel> allReceivedMails();

    ReceiveEmailViewModel getViewModelById(String id);

    int countNew(String principal);
}
