package dst.abc_bg.services.impl;

import dst.abc_bg.entities.ReceiveEmail;
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
    private static final String EMAIL_IN_BRACKETS_PATTERN = "<(.+)>";

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

    private Set<ReceiveEmail> getMessagesAsSetOfReceiveEmails(Pattern pattern, Message[] messages, int newMessagesCount) throws IOException, MessagingException {
        Set<ReceiveEmail> newMessages = new LinkedHashSet<>();
        Matcher matcher;
        for (int i = messages.length - newMessagesCount; i < messages.length; i++) {
//            String username = messages[i].getContent().toString().split(" ")[1].split("@")[0];
//            UserServiceModel recipient = this.userService.getUserServiceModelByUsername(username);
//            receiveEmail.setRecipient(this.mapper.map(recipient, User.class));
            ReceiveEmail receiveEmail = new ReceiveEmail();
            String sender = messages[i].getFrom()[0].toString();
            matcher = pattern.matcher(sender);
            if (matcher.find()) {
                receiveEmail.setSender(matcher.group(1));
            }
            receiveEmail.setSubject(messages[i].getSubject());
            receiveEmail.setContent(messages[i].getContent().toString());
            receiveEmail.setSentOn(new Timestamp(messages[i].getSentDate().getTime()).toLocalDateTime());

            newMessages.add(receiveEmail);
        }

        return newMessages;
    }

    @Override
    public Set<ReceiveEmail> receiveEmails() throws Exception {
        Pattern pattern = Pattern.compile(EMAIL_IN_BRACKETS_PATTERN);
        Message[] messages = this.emailReceiver.receiveEmail();
        int newMessagesCount = getNewMessagesCount(messages.length);

        Set<ReceiveEmail> newMessages = getMessagesAsSetOfReceiveEmails(pattern, messages, newMessagesCount);
        saveEmail(newMessages);

        return newMessages;
    }

    @Override
    public Set<ReceiveEmailViewModel> allNonDeletedReceivedMailsForUser(String username) {
        Set<ReceiveEmail> allByDeletedOnNull = this.emailRepository.getAllByDeletedOnNull();
        Set<ReceiveEmailViewModel> receiveEmailViewModels = this.mapListOfEmailToListOfViewModel(allByDeletedOnNull);

        return receiveEmailViewModels;
    }

    @Override
    public ReceiveEmailViewModel getNonDeletedReceivedEmailViewModelById(String id) {
        ReceiveEmailViewModel viewModel = this.mapper.map(this.emailRepository.getByIdEqualsAndDeletedOnNull(id), ReceiveEmailViewModel.class);
        return viewModel;
    }

    @Override
    public boolean deleteMail(String id) {
        ReceiveEmail email = this.emailRepository.getByIdEqualsAndDeletedOnNull(id);
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
