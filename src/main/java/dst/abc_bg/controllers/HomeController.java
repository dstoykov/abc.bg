package dst.abc_bg.controllers;

import dst.abc_bg.areas.email.receive.services.ReceiveEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {
    private final ReceiveEmailService receiveEmailService;

    @Autowired
    public HomeController(ReceiveEmailService receiveEmailService) {
        this.receiveEmailService = receiveEmailService;
    }

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView, Model model, Principal principal) {
        modelAndView.setViewName("home");
        modelAndView.addObject("title", "Home");
        modelAndView.addObject("newMails", this.receiveEmailService.countNew(principal.getName()));
        if (model.containsAttribute("success")) {
            modelAndView.addObject("success");
        }

        return modelAndView;
    }
}
