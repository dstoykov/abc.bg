package dst.abc_bg.areas.user.controllers;

import dst.abc_bg.areas.user.exceptions.PasswordsMismatchException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyExistsException;
import dst.abc_bg.areas.user.models.binding.UserEditDataBindingModel;
import dst.abc_bg.areas.user.models.binding.UserEditPasswordBindingModel;
import dst.abc_bg.areas.user.models.binding.UserRegisterBindingModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ModelMapper mapper;

    @Autowired
    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, ModelAndView modelAndView, Model model) {
        modelAndView.setViewName("users-login");
        modelAndView.addObject("title", "Login");
        if (model.containsAttribute("success")) {
            modelAndView.addObject("success");
        }
            modelAndView.addObject("loginError", error);

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, Model model) {
        modelAndView.setViewName("users-register");
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
    public ModelAndView register(@Valid @ModelAttribute(name = "registerInput") UserRegisterBindingModel userBindingModel, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerInput", bindingResult);
            redirectAttributes.addFlashAttribute("registerInput", userBindingModel);
            modelAndView.setViewName("redirect:register");
        } else {
            try {
                this.userService.register(userBindingModel);
                modelAndView.setViewName("redirect:login");
                redirectAttributes.addFlashAttribute("success", "success");
            } catch (PasswordsMismatchException e) {
                redirectAttributes.addFlashAttribute("passwordError", "error");
                redirectAttributes.addFlashAttribute("registerInput", userBindingModel);
                modelAndView.setViewName("redirect:register");
            }

        }

        return modelAndView;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(ModelAndView modelAndView, Model model, Principal principal) throws UserAlreadyBannedException {
        UserServiceModel userServiceModel = this.userService.getNonDeletedUserServiceModelByUsername(principal.getName());
        modelAndView.setViewName("users-profile");
        modelAndView.addObject("title", "Edit Profile");
        modelAndView.addObject("username", principal.getName());

        if (!model.containsAttribute("userDataInput")) {
            UserEditDataBindingModel userEditDataBindingModel = this.mapper.map(userServiceModel, UserEditDataBindingModel.class);
            model.addAttribute("userDataInput", userEditDataBindingModel);
        }
        if (!model.containsAttribute("userPasswordInput")) {
            UserEditPasswordBindingModel userEditPasswordBindingModel = this.mapper.map(userServiceModel, UserEditPasswordBindingModel.class);
            model.addAttribute("userPasswordInput", userEditPasswordBindingModel);
        }
        if (model.containsAttribute("passwordError")) {
            modelAndView.addObject("passwordError");
        }
        if (model.containsAttribute("done")) {
            modelAndView.addObject("done");
        }

        return modelAndView;
    }

    @PostMapping("/profile/edit-data")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileData(@Valid @ModelAttribute(name = "userDataInput") UserEditDataBindingModel userEditDataBindingModel, BindingResult bindingResult, ModelAndView modelAndView, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDataInput", bindingResult);
            redirectAttributes.addFlashAttribute("userDataInput", userEditDataBindingModel);
        } else {
            this.userService.editUserDataByUsername(userEditDataBindingModel, principal.getName());
            redirectAttributes.addFlashAttribute("done", "done");
        }
        modelAndView.setViewName("redirect:../profile");

        return modelAndView;
    }

    @PostMapping("/profile/edit-password")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfilePassword(@Valid @ModelAttribute(name = "userPasswordInput") UserEditPasswordBindingModel userEditPasswordBindingModel, BindingResult bindingResult, ModelAndView modelAndView, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordInput", bindingResult);
            redirectAttributes.addFlashAttribute("userPasswordInput", userEditPasswordBindingModel);
        } else {
            try {
                this.userService.editUserPasswordByUsername(userEditPasswordBindingModel, principal.getName());
                redirectAttributes.addFlashAttribute("done", "done");
            } catch (PasswordsMismatchException e) {
                redirectAttributes.addFlashAttribute("passwordError", "error");
            }
        }
        modelAndView.setViewName("redirect:../profile");

        return modelAndView;
    }
}
