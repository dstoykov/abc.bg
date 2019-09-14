package dst.abc_bg.areas.email.controllers.admin;

import dst.abc_bg.areas.email.receive.models.view.ReceiveEmailViewModel;
import dst.abc_bg.areas.email.send.models.view.SendEmailViewModel;
import dst.abc_bg.areas.email.receive.services.ReceiveEmailService;
import dst.abc_bg.areas.email.send.services.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequestMapping("/admin/mails")
public class AdminEmailController {
    private final SendEmailService sendEmailService;
    private final ReceiveEmailService receiveEmailService;

    @Autowired
    public AdminEmailController(SendEmailService sendEmailService, ReceiveEmailService receiveEmailService) {
        this.sendEmailService = sendEmailService;
        this.receiveEmailService = receiveEmailService;
    }

    @GetMapping("/sent")
    public ModelAndView allSentMails(ModelAndView modelAndView) {
        modelAndView.setViewName("admin-mails-sent");
        modelAndView.addObject("title", "All sent E-mails");
        Set<SendEmailViewModel> sendEmailViewModels = this.sendEmailService.allSentMails();
        modelAndView.addObject("mails", sendEmailViewModels);

        return modelAndView;
    }

    @GetMapping("/sent/{id}")
    public ModelAndView sentEmailDetails(@PathVariable(name = "id") String id, ModelAndView modelAndView) {
        modelAndView.setViewName("admin-mail-sent-details");
        modelAndView.addObject("title", "Details");
        SendEmailViewModel viewModel = this.sendEmailService.getSendEmailViewModelById(id);
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }

    @GetMapping("/inbox")
    public ModelAndView allInboxMails(ModelAndView modelAndView) {
        modelAndView.setViewName("admin-mails-inbox");
        modelAndView.addObject("title", "All received E-mails");
        Set<ReceiveEmailViewModel> receiveEmailViewModels = this.receiveEmailService.allReceivedMails();
        modelAndView.addObject("mails", receiveEmailViewModels);

        return modelAndView;
    }

    @GetMapping("/inbox/{id}")
    public ModelAndView inboxEmailDetails(@PathVariable(name = "id") String id, ModelAndView modelAndView) {
        modelAndView.setViewName("admin-mail-inbox-details");
        modelAndView.addObject("title", "Details");
        ReceiveEmailViewModel viewModel = this.receiveEmailService.getViewModelById(id);
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }
}
