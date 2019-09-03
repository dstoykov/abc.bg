package dst.abc_bg.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping("/error")
    public ModelAndView error(ModelAndView modelAndView) {
        modelAndView.setViewName("error/default-error-page");
        modelAndView.addObject("title", "Oops...");
        return modelAndView;
    }
}
