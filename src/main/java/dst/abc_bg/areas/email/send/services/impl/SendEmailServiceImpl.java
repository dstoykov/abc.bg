package dst.abc_bg.areas.email.send.services.impl;

import dst.abc_bg.areas.email.send.entities.SendEmail;
import dst.abc_bg.areas.user.entities.User;
import dst.abc_bg.areas.email.exceptions.CannotAccessMailException;
import dst.abc_bg.areas.email.send.models.binding.SendEmailNewBindingModel;
import dst.abc_bg.areas.email.send.models.service.SendEmailServiceModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.email.send.models.view.SendEmailViewModel;
import dst.abc_bg.areas.email.send.repositories.SendEmailRepository;
import dst.abc_bg.areas.email.send.services.SendEmailService;
import dst.abc_bg.areas.user.services.UserService;
import dst.abc_bg.areas.email.send.util.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
public class SendEmailServiceImpl implements SendEmailService {
    private static final String CANNOT_ACCESS_MAIL_EXCEPTION_MSG = "You cannot access this E-mail";

    private static final String FROM = "From";
    private static final String SPACE = " ";
    private static final String AT = "@";
    private static final String DOMAIN = "abc.bg";

    private static final String SENDER_EMAIL_REGEX = "From [a-zA-Z0-9.-_]+@abc\\.bg";
    private static final String EMPTY_STRING = "";

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

    private boolean saveEmail(SendEmailServiceModel emailServiceModel) {
        SendEmail email = this.mapper.map(emailServiceModel, SendEmail.class);
        this.emailRepository.save(email);

        return true;
    }

    private boolean checkIfEmailIsNull(SendEmail email) throws CannotAccessMailException {
        if (email == null) {
            throw new CannotAccessMailException(CANNOT_ACCESS_MAIL_EXCEPTION_MSG);
        }

        return true;
    }

    @Override
    public SendEmailServiceModel sendEmail(SendEmailNewBindingModel bindingModel, String sender) throws MessagingException {
        SendEmailServiceModel emailServiceModel = this.mapper.map(bindingModel, SendEmailServiceModel.class);
        emailServiceModel.setSender(userService.getUserServiceModelByUsername(sender));
        emailServiceModel.setSentOn(LocalDateTime.now());
        emailServiceModel.setContent(this.formatEmailContent(emailServiceModel));

        this.emailSender.sendEmail(emailServiceModel);

        emailServiceModel.setContent(emailServiceModel.getContent().replaceFirst(SENDER_EMAIL_REGEX, EMPTY_STRING));

        this.saveEmail(emailServiceModel);

        return emailServiceModel;
    }

    @Override
    public Set<SendEmailViewModel> allNonDeletedSentMailsForUser(String name) {
        UserServiceModel userServiceModel = this.userService.getUserServiceModelByUsername(name);
        Set<SendEmail> emails = this.emailRepository.getAllBySenderAndDeletedOnNullOrderBySentOnDesc(this.mapper.map(userServiceModel, User.class));

        Set<SendEmailViewModel> viewModels = this.mapListOfEmailToListOfViewModel(emails);
        return viewModels;
    }

    @Override
    public SendEmailViewModel getNonDeletedSendEmailViewModelById(String id, String name) throws CannotAccessMailException {
        UserServiceModel userServiceModel = this.userService.getUserServiceModelByUsername(name);
        SendEmail email = this.emailRepository.getByIdEqualsAndSenderEqualsAndDeletedOnNull(id, this.mapper.map(userServiceModel, User.class));

        this.checkIfEmailIsNull(email);

        SendEmailViewModel viewModel = this.mapper.map(email, SendEmailViewModel.class);

        return viewModel;
    }

    @Override
    public boolean deleteMail(String id, String name) throws CannotAccessMailException {
        UserServiceModel userServiceModel = this.userService.getUserServiceModelByUsername(name);
        SendEmail email = this.emailRepository.getByIdEqualsAndSenderEqualsAndDeletedOnNull(id, this.mapper.map(userServiceModel, User.class));

        this.checkIfEmailIsNull(email);

        email.setDeletedOn(LocalDateTime.now());
        this.emailRepository.save(email);

        return true;
    }

    @Override
    public Set<SendEmailViewModel> allSentMails() {
        Set<SendEmail> allByIdNotNullOrderBySentOnDesc = this.emailRepository.findAllByIdNotNullOrderBySentOnDesc();
        Set<SendEmailViewModel> viewModels = this.mapListOfEmailToListOfViewModel(allByIdNotNullOrderBySentOnDesc);

        return viewModels;
    }

    @Override
    public SendEmailViewModel getSendEmailViewModelById(String id) {
        SendEmail one = this.emailRepository.getOne(id);
        SendEmailViewModel viewModel = this.mapper.map(one, SendEmailViewModel.class);

        return viewModel;
    }
}
