package dst.abc_bg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView, Principal principal) {
        if (principal == null) {
            return new ModelAndView("redirect:/users/login");
        }
        modelAndView.setViewName("home");
        modelAndView.addObject("title", "Home");

        return modelAndView;
    }
}
