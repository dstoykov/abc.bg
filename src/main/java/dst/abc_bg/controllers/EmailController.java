package dst.abc_bg.controllers;

import dst.abc_bg.exceptions.CannotAccessMailException;
import dst.abc_bg.models.binding.SendEmailNewBindingModel;
import dst.abc_bg.models.view.ReceiveEmailViewModel;
import dst.abc_bg.models.view.SendEmailViewModel;
import dst.abc_bg.services.ReceiveEmailService;
import dst.abc_bg.services.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/mails")
public class EmailController {
    private final SendEmailService sendEmailService;
    private final ReceiveEmailService receiveEmailService;

    @Autowired
    public EmailController(SendEmailService emailService, ReceiveEmailService receiveEmailService) {
        this.sendEmailService = emailService;
        this.receiveEmailService = receiveEmailService;
    }

    @GetMapping("/new")
    public ModelAndView newMessage(ModelAndView modelAndView, Model model) {
        modelAndView.setViewName("mail-new");
        modelAndView.addObject("title", "New mail");
        if (!model.containsAttribute("mailInput")) {
            model.addAttribute("mailInput", new SendEmailNewBindingModel());
        }

        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView newMessage(@Valid @ModelAttribute(name = "mailInput") SendEmailNewBindingModel bindingModel, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes, Principal principal) throws MessagingException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.mailInput", bindingResult);
            redirectAttributes.addFlashAttribute("mailInput", bindingModel);
            modelAndView.setViewName("redirect:new");
        } else {
            this.sendEmailService.sendEmail(bindingModel, principal.getName());
            modelAndView.setViewName("redirect:/");
            redirectAttributes.addFlashAttribute("success", "success");
        }

        return modelAndView;
    }

    @GetMapping("/sent")
    public ModelAndView sentMails(ModelAndView modelAndView, Principal principal) {
        modelAndView.setViewName("mails-sent");
        modelAndView.addObject("title", "Sent mails");
        modelAndView.addObject("mails", this.sendEmailService.allNonDeletedSentMailsForUser(principal.getName()));

        return modelAndView;
    }

    @GetMapping("/sent/{id}")
    public ModelAndView sentMailDetails(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        SendEmailViewModel viewModel = this.sendEmailService.getNonDeletedSendEmailViewModelById(id, principal.getName());
        modelAndView.setViewName("mail-sent-details");
        modelAndView.addObject("title", "Details");
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }

    @GetMapping("/sent/delete/{id}")
    public ModelAndView deleteSentMail(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        SendEmailViewModel viewModel = this.sendEmailService.getNonDeletedSendEmailViewModelById(id, principal.getName());
        modelAndView.setViewName("mail-sent-delete");
        modelAndView.addObject("title", "Delete E-mail");
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }

    @PostMapping("/sent/delete/{id}")
    public ModelAndView deleteSentMailConfirm(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        this.sendEmailService.deleteMail(id, principal.getName());
        modelAndView.setViewName("redirect:/mails/sent");

        return modelAndView;
    }

    @GetMapping("/inbox")
    public ModelAndView inboxMails(ModelAndView modelAndView, Principal principal) {
        Set<ReceiveEmailViewModel> receiveEmailViewModels = this.receiveEmailService.allNonDeletedReceivedMailsForUser(principal.getName());

        modelAndView.setViewName("mails-inbox");
        modelAndView.addObject("title", "Inbox");
        modelAndView.addObject("mails", receiveEmailViewModels);
        modelAndView.addObject("areNew", this.receiveEmailService.areNew(receiveEmailViewModels));

        return modelAndView;
    }

    @GetMapping("/inbox/{id}")
    public ModelAndView inboxMailDetails(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        modelAndView.setViewName("mail-inbox-details");
        modelAndView.addObject("title", "Details");
        ReceiveEmailViewModel viewModel = this.receiveEmailService.getNonDeletedReceivedEmailViewModelById(id, principal.getName());
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }

    @GetMapping("/inbox/delete/{id}")
    public ModelAndView deleteInboxMail(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        modelAndView.setViewName("mail-inbox-delete");
        modelAndView.addObject("title", "Details");
        ReceiveEmailViewModel viewModel = this.receiveEmailService.getNonDeletedReceivedEmailViewModelById(id, principal.getName());
        modelAndView.addObject("mail", viewModel);

        return modelAndView;
    }

    @PostMapping("/inbox/delete/{id}")
    public ModelAndView deleteInboxMailConfirm(@PathVariable(name = "id") String id, ModelAndView modelAndView, Principal principal) throws CannotAccessMailException {
        this.receiveEmailService.deleteMail(id, principal.getName());
        modelAndView.setViewName("redirect:/mails/inbox");

        return modelAndView;
    }
}
