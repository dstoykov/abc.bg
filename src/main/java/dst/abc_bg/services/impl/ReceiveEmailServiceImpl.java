package dst.abc_bg.services.impl;

import dst.abc_bg.entities.ReceiveEmail;
import dst.abc_bg.entities.User;
import dst.abc_bg.exceptions.CannotAccessMailException;
import dst.abc_bg.models.service.UserServiceModel;
import dst.abc_bg.models.view.ReceiveEmailViewModel;
import dst.abc_bg.repositories.ReceiveEmailRepository;
import dst.abc_bg.services.ReceiveEmailService;
import dst.abc_bg.services.UserService;
import dst.abc_bg.util.EmailReceiver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ReceiveEmailServiceImpl implements ReceiveEmailService {
    private static final String CANNOT_ACCESS_MAIL_EXCEPTION_MSG = "You cannot access this E-mail";

    private static final String EMAIL_IN_BRACKETS_PATTERN = "<(.+)>";
    private static final String RECIPIENT_SUBJECT_PATTERN = "^To ([a-zA-z0-9.-_]+)@abc.bg(.+)$";

    private final ReceiveEmailRepository emailRepository;
    private final EmailReceiver emailReceiver;
    private final UserService userService;
    private final ModelMapper mapper;

    @Autowired
    public ReceiveEmailServiceImpl(ReceiveEmailRepository emailRepository, EmailReceiver emailReceiver, UserService userService, ModelMapper mapper) {
        this.emailRepository = emailRepository;
        this.emailReceiver = emailReceiver;
        this.userService = userService;
        this.mapper = mapper;
    }

    private Set<ReceiveEmailViewModel> mapListOfEmailToListOfViewModel(Set<ReceiveEmail> emails) {
        Set<ReceiveEmailViewModel> viewModels = new LinkedHashSet<>();
        for (ReceiveEmail email : emails) {
            viewModels.add(this.mapper.map(email, ReceiveEmailViewModel.class));
        }

        return viewModels;
    }

    private boolean saveEmail(Set<ReceiveEmail> newMessages) {
        this.emailRepository.saveAll(newMessages);
        return true;
    }

    private int getNewMessagesCount(int messagesLength) {
        int all = this.emailRepository.findAll().size();
        return messagesLength - all;
    }

    private boolean checkIfEmailIsNull(ReceiveEmail email) throws CannotAccessMailException {
        if (email == null) {
            throw new CannotAccessMailException(CANNOT_ACCESS_MAIL_EXCEPTION_MSG);
        }

        return true;
    }

    private Set<ReceiveEmail> getMessagesAsSetOfReceiveEmails(Pattern senderEmail, Pattern recipientUsername, Set<Message> messages, int newMessagesCount) throws IOException, MessagingException {
        Set<ReceiveEmail> newMessages = new LinkedHashSet<>();
        int i = 0;
        for (Message message : messages) {
            if (i >= newMessagesCount) {
                break;
            }
            ReceiveEmail receiveEmail = new ReceiveEmail();
            addRecipientToMail(message, receiveEmail, recipientUsername);
            addSenderToMail(senderEmail, message, receiveEmail);
            receiveEmail.setContent(message.getContent().toString());
            receiveEmail.setSentOn(new Timestamp(message.getSentDate().getTime()).toLocalDateTime());

            newMessages.add(receiveEmail);
            i++;
        }

        return newMessages;
    }

    private String addRecipientToMail(Message message, ReceiveEmail receiveEmail, Pattern recipientUsername) throws MessagingException {
        String subjectLine = message.getSubject();
        Matcher matcher = recipientUsername.matcher(subjectLine);
        if (matcher.find()) {
            UserServiceModel recipient = this.userService.getUserServiceModelByUsername(matcher.group(1));
            receiveEmail.setRecipient(this.mapper.map(recipient, User.class));
            addSubjectToMail(receiveEmail, matcher);

            return recipient.getUsername();
        }
        return null;
    }

    private String addSubjectToMail(ReceiveEmail receiveEmail, Matcher matcher) {
        receiveEmail.setSubject(matcher.group(2));
        return receiveEmail.getSubject();
    }

    private boolean addSenderToMail(Pattern senderEmail, Message message, ReceiveEmail receiveEmail) throws MessagingException {
        String sender = message.getFrom()[0].toString();
        Matcher matcher = senderEmail.matcher(sender);
        if (matcher.find()) {
            receiveEmail.setSender(matcher.group(1));
            return true;
        }
        return false;
    }

    @Override
    public Set<ReceiveEmail> receiveEmails() throws Exception {
        Pattern senderEmail = Pattern.compile(EMAIL_IN_BRACKETS_PATTERN);
        Pattern recipientUsername = Pattern.compile(RECIPIENT_SUBJECT_PATTERN);
        Set<Message> messages = this.emailReceiver.receiveEmail();
        int newMessagesCount = getNewMessagesCount(messages.size());

        Set<ReceiveEmail> newMessages = getMessagesAsSetOfReceiveEmails(senderEmail, recipientUsername, messages, newMessagesCount);
        saveEmail(newMessages);

        return newMessages;
    }

    @Override
    public Set<ReceiveEmailViewModel> allNonDeletedReceivedMailsForUser(String username) {
        Set<ReceiveEmail> allByDeletedOnNull = this.emailRepository.getAllByRecipientAndDeletedOnNull(username);
        Set<ReceiveEmailViewModel> receiveEmailViewModels = this.mapListOfEmailToListOfViewModel(allByDeletedOnNull);

        return receiveEmailViewModels;
    }

    @Override
    public ReceiveEmailViewModel getNonDeletedReceivedEmailViewModelById(String id, String name) throws CannotAccessMailException {
        ReceiveEmail receiveEmail = this.emailRepository.getByIdRecipientAndDeletedOnNull(id, name);
        checkIfEmailIsNull(receiveEmail);
        ReceiveEmailViewModel viewModel = this.mapper.map(receiveEmail, ReceiveEmailViewModel.class);

        return viewModel;
    }

    @Override
    public boolean deleteMail(String id, String name) throws CannotAccessMailException {
        ReceiveEmail email = this.emailRepository.getByIdRecipientAndDeletedOnNull(id, name);
        checkIfEmailIsNull(email);
        email.setDeletedOn(LocalDateTime.now());

        this.emailRepository.save(email);

        return true;
    }

    @Override
    public Set<ReceiveEmailViewModel> allReceivedMails() {
        Set<ReceiveEmail> allByIdNotNullOrderBySentOnDesc = this.emailRepository.findAllByIdNotNullOrderBySentOnDesc();
        Set<ReceiveEmailViewModel> viewModels = this.mapListOfEmailToListOfViewModel(allByIdNotNullOrderBySentOnDesc);

        return viewModels;
    }

    @Override
    public ReceiveEmailViewModel getViewModelById(String id) {
        ReceiveEmail one = this.emailRepository.getOne(id);
        ReceiveEmailViewModel viewModel = this.mapper.map(one, ReceiveEmailViewModel.class);

        return viewModel;
    }
}
