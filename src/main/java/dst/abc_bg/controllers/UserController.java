package dst.abc_bg.controllers;

import dst.abc_bg.exceptions.PasswordsMismatchException;
import dst.abc_bg.exceptions.UserAlreadyExistsException;
import dst.abc_bg.models.binding.UserRegisterBindingModel;
import dst.abc_bg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, Model model, Principal principal) {
        if (principal != null) {
            return new ModelAndView("redirect:/");
        }
        modelAndView.setViewName("login");
        modelAndView.addObject("title", "Login");
        if (model.containsAttribute("success")) {
            modelAndView.addObject("success");
        }

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, Model model, Principal principal) {
        if (principal != null) {
            return new ModelAndView("redirect:/");
        }
        modelAndView.setViewName("register");
        modelAndView.addObject("title", "Register");
        if (!model.containsAttribute("registerInput")) {
            model.addAttribute("registerInput", new UserRegisterBindingModel());
        }
        if (model.containsAttribute("passwordError")) {
            modelAndView.addObject("passwordError");
        }
        if (model.containsAttribute("userExistsError")) {
            modelAndView.addObject("userExistsError");
        }

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute(name = "registerInput") UserRegisterBindingModel userBindingModel, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) throws  UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerInput", bindingResult);
            redirectAttributes.addFlashAttribute("registerInput", userBindingModel);
            modelAndView.setViewName("redirect:register");
        } else {
            try {
                this.userService.register(userBindingModel);
                modelAndView.setViewName("redirect:login");
                redirectAttributes.addFlashAttribute("success", "Successfully registered");
            } catch (PasswordsMismatchException e) {
                redirectAttributes.addFlashAttribute("passwordError", "error");
                redirectAttributes.addFlashAttribute("registerInput", userBindingModel);
                modelAndView.setViewName("redirect:register");
            }

        }

        return modelAndView;
    }
}
