package dst.abc_bg.services.impl;

import dst.abc_bg.entities.SendEmail;
import dst.abc_bg.entities.User;
import dst.abc_bg.models.binding.SendEmailNewBindingModel;
import dst.abc_bg.models.service.SendEmailServiceModel;
import dst.abc_bg.models.service.UserServiceModel;
import dst.abc_bg.models.view.SendEmailViewModel;
import dst.abc_bg.repositories.SendEmailRepository;
import dst.abc_bg.services.SendEmailService;
import dst.abc_bg.services.UserService;
import dst.abc_bg.util.EmailReceiver;
import dst.abc_bg.util.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
public class SendEmailServiceImpl implements SendEmailService {
    private static final String FROM = "From";
    private static final String SPACE = " ";
    private static final String AT = "@";
    private static final String DOMAIN = "abc.bg";

    private final SendEmailRepository emailRepository;
    private final UserService userService;
    private final ModelMapper mapper;
    private final EmailSender emailSender;

    @Autowired
    public SendEmailServiceImpl(SendEmailRepository emailRepository, UserService userService, ModelMapper mapper, EmailSender emailSender) {
        this.emailRepository = emailRepository;
        this.userService = userService;
        this.mapper = mapper;
        this.emailSender = emailSender;
    }

    private Set<SendEmailViewModel> mapListOfEmailToListOfViewModel(Set<SendEmail> emails) {
        Set<SendEmailViewModel> viewModels = new LinkedHashSet<>();
        for (SendEmail email : emails) {
            viewModels.add(this.mapper.map(email, SendEmailViewModel.class));
        }

        return viewModels;
    }

    private String formatEmailContent(SendEmailServiceModel serviceModel) {
        StringBuilder formattedContent = new StringBuilder();
        formattedContent.append(FROM)
                .append(SPACE)
                .append(serviceModel.getSender().getUsername())
                .append(AT)
                .append(DOMAIN)
                .append(System.lineSeparator())
                .append(serviceModel.getContent());

        return formattedContent.toString();
    }

    @Override
    public SendEmailServiceModel sendEmail(SendEmailNewBindingModel bindingModel, String sender) throws MessagingException {
        SendEmailServiceModel emailServiceModel = this.mapper.map(bindingModel, SendEmailServiceModel.class);
        emailServiceModel.setSender(userService.getUserServiceModelByUsername(sender));
        emailServiceModel.setSentOn(LocalDateTime.now());
        emailServiceModel.setContent(this.formatEmailContent(emailServiceModel));

        this.emailSender.sendEmail(emailServiceModel);

        SendEmail email = this.mapper.map(emailServiceModel, SendEmail.class);
        this.emailRepository.save(email);

        return emailServiceModel;
    }

    @Override
    public Set<SendEmailViewModel> allNonDeletedSentMailsFromUser(String name) {
        UserServiceModel userServiceModel = this.userService.getUserServiceModelByUsername(name);
        Set<SendEmail> emails = this.emailRepository.getAllBySenderAndDeletedOnNullOrderBySentOnDesc(this.mapper.map(userServiceModel, User.class));

        Set<SendEmailViewModel> viewModels = this.mapListOfEmailToListOfViewModel(emails);
        return viewModels;
    }
}
