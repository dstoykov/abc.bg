package dst.abc_bg.areas.email.receive.services.impl;

import dst.abc_bg.areas.email.receive.entities.ReceiveEmail;
import dst.abc_bg.areas.user.entities.User;
import dst.abc_bg.areas.email.exceptions.CannotAccessMailException;
import dst.abc_bg.areas.email.receive.models.service.ReceiveEmailServiceModel;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.email.receive.models.view.ReceiveEmailViewModel;
import dst.abc_bg.areas.email.receive.repositories.ReceiveEmailRepository;
import dst.abc_bg.areas.email.receive.services.ReceiveEmailService;
import dst.abc_bg.areas.user.services.UserService;
import dst.abc_bg.areas.email.receive.util.EmailReceiver;
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
    private static final Integer INITIAL_INDEX = 0;
    private static final Integer MATCHER_GROUP_ONE = 1;
    private static final Integer MATCHER_GROUP_TWO = 2;

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

    private Set<ReceiveEmailServiceModel> mapListOfEmailToListOfServiceModel(Set<ReceiveEmail> emails) {
        Set<ReceiveEmailServiceModel> serviceModels = new LinkedHashSet<>();
        for (ReceiveEmail email : emails) {
            serviceModels.add(this.mapper.map(email, ReceiveEmailServiceModel.class));
        }

        return serviceModels;
    }

    private boolean saveEmails(Set<ReceiveEmail> newMessages) {
        this.emailRepository.saveAll(newMessages);
        return true;
    }

    private boolean saveEmail(ReceiveEmail receiveEmail) {
        this.emailRepository.save(receiveEmail);
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

    private Set<ReceiveEmail> getMessagesAsSetOfReceiveEmails(Pattern senderEmail, Pattern recipientUsername, Set<Message> messages, int newMessagesCount) throws IOException, MessagingException, UserAlreadyBannedException {
        Set<ReceiveEmail> newMessages = new LinkedHashSet<>();
        int i = INITIAL_INDEX;
        for (Message message : messages) {
            if (i >= newMessagesCount) {
                break;
            }
            Matcher recipientUsernameAndSubjectMatcher = this.getRecipientUsernameAndSubjectMatcher(message, recipientUsername);
            ReceiveEmail receiveEmail = new ReceiveEmail();
            if (!addRecipientToMail(recipientUsernameAndSubjectMatcher, receiveEmail)) {
                i++;
                continue;
            }
            addSubjectToMail(recipientUsernameAndSubjectMatcher, receiveEmail);
            addSenderToMail(senderEmail, message, receiveEmail);
            receiveEmail.setContent(message.getContent().toString());
            receiveEmail.setSentOn(new Timestamp(message.getSentDate().getTime()).toLocalDateTime());
            receiveEmail.setNew(true);

            newMessages.add(receiveEmail);
            i++;
        }

        return newMessages;
    }

    private Matcher getRecipientUsernameAndSubjectMatcher(Message message, Pattern recipientUsername) throws MessagingException {
        String subjectLine = message.getSubject();
        Matcher matcher = recipientUsername.matcher(subjectLine);

        return matcher;
    }

    private boolean addRecipientToMail(Matcher matcher, ReceiveEmail receiveEmail) throws UserAlreadyBannedException {
        if (matcher.find()) {
            UserServiceModel recipient = this.userService.getNonDeletedUserServiceModelByUsername(matcher.group(1));
            receiveEmail.setRecipient(this.mapper.map(recipient, User.class));

            return true;
        }
        return false;
    }

    private String addSubjectToMail(Matcher matcher, ReceiveEmail receiveEmail) {
        receiveEmail.setSubject(matcher.group(MATCHER_GROUP_TWO));
        return receiveEmail.getSubject();
    }

    private boolean addSenderToMail(Pattern senderEmail, Message message, ReceiveEmail receiveEmail) throws MessagingException {
        String sender = message.getFrom()[INITIAL_INDEX].toString();
        Matcher matcher = senderEmail.matcher(sender);
        if (matcher.find()) {
            receiveEmail.setSender(matcher.group(MATCHER_GROUP_ONE));
            return true;
        }
        return false;
    }

    private ReceiveEmail setEmailAsOpened(ReceiveEmail receiveEmail) {
        receiveEmail.setNew(false);
        this.saveEmail(receiveEmail);

        return receiveEmail;
    }

    @Override
    public Set<ReceiveEmailServiceModel> receiveEmails() throws MessagingException, IOException, UserAlreadyBannedException {
        Pattern senderEmail = Pattern.compile(EMAIL_IN_BRACKETS_PATTERN);
        Pattern recipientUsername = Pattern.compile(RECIPIENT_SUBJECT_PATTERN);
        Set<Message> messages = this.emailReceiver.receiveEmail();
        int newMessagesCount = getNewMessagesCount(messages.size());

        Set<ReceiveEmail> newMessages = getMessagesAsSetOfReceiveEmails(senderEmail, recipientUsername, messages, newMessagesCount);
        saveEmails(newMessages);

        return this.mapListOfEmailToListOfServiceModel(newMessages);
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

        this.setEmailAsOpened(receiveEmail);

        return viewModel;
    }

    @Override
    public Boolean deleteMail(String id, String name) throws CannotAccessMailException {
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

    @Override
    public int countNew(String principal) {
        Set<ReceiveEmail> receiveEmails = this.emailRepository.getAllByRecipientAndDeletedOnNull(principal);
        int newMails = 0;
        for (ReceiveEmail receiveEmail : receiveEmails) {
            if (receiveEmail.getNew()) {
                newMails++;
            }
        }

        return newMails;
    }
}
