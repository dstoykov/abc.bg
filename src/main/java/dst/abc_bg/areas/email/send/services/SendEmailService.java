package dst.abc_bg.areas.email.send.services;

import dst.abc_bg.areas.email.exceptions.CannotAccessMailException;
import dst.abc_bg.areas.email.send.models.binding.SendEmailNewBindingModel;
import dst.abc_bg.areas.email.send.models.service.SendEmailServiceModel;
import dst.abc_bg.areas.email.send.models.view.SendEmailViewModel;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;

import javax.mail.MessagingException;
import java.util.Set;

public interface SendEmailService {
    SendEmailServiceModel sendEmail(SendEmailNewBindingModel bindingModel, String sender) throws MessagingException, UserAlreadyBannedException;

    Set<SendEmailViewModel> allNonDeletedSentMailsForUser(String name) throws UserAlreadyBannedException;

    SendEmailViewModel getNonDeletedSendEmailViewModelById(String id, String name) throws CannotAccessMailException, UserAlreadyBannedException;

    boolean deleteMail(String id, String principal) throws CannotAccessMailException, UserAlreadyBannedException;

    Set<SendEmailViewModel> allSentMails();

    SendEmailViewModel getSendEmailViewModelById(String id);
}
