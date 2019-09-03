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
import org.springframework.web.servlet.ModelAndView;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ReceiveEmailServiceImpl implements ReceiveEmailService {
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

    @Override
    public Set<ReceiveEmail> receiveEmails() throws Exception {
        Pattern pattern = Pattern.compile("<(.+)>");
        Matcher matcher;
        Message[] messages = this.emailReceiver.receiveEmail();
        int all = this.emailRepository.findAll().size();
        int newMessagesCount = messages.length - all;

        Set<ReceiveEmail> newMessages = new LinkedHashSet<>();
        for (int i = 0; i < newMessagesCount; i++) {
            String username = messages[i].getContent().toString();
//            username = username.split(" ")[1].split("@")[0];
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

        this.emailRepository.saveAll(newMessages);

        return newMessages;
    }

    @Override
    public Set<ReceiveEmailViewModel> allNonDeletedReceivedMailsForUser(String username) {
        Set<ReceiveEmail> allByDeletedOnNull = this.emailRepository.getAllByDeletedOnNull();
        Set<ReceiveEmailViewModel> receiveEmailViewModels = this.mapListOfEmailToListOfViewModel(allByDeletedOnNull);

        return receiveEmailViewModels;
    }
}
