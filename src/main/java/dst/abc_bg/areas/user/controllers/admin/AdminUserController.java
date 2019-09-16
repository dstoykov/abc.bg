package dst.abc_bg.areas.user.controllers.admin;

import dst.abc_bg.areas.user.exceptions.PasswordsMismatchException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.exceptions.UserNotBannedException;
import dst.abc_bg.areas.user.models.binding.UserEditDataBindingModel;
import dst.abc_bg.areas.user.models.binding.UserEditPasswordBindingModel;
import dst.abc_bg.areas.user.models.service.UserServiceModel;
import dst.abc_bg.areas.user.models.view.UserViewModel;
import dst.abc_bg.areas.user.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private static final String PROFILE_PAGE_TITLE_FORMAT = "%s's profile";
    private static final String PROFILE_BAN_TITLE_FORMAT = "Ban %s";
    private static final String PROFILE_RESTORE_TITLE_FORMAT = "Restore %s";

    private final UserService userService;
    private final ModelMapper mapper;

    @Autowired
    public AdminUserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("all")
    public ModelAndView allUsers(ModelAndView modelAndView, Model model) {
        Set<UserViewModel> userViewModels = this.userService.getListWithViewModels();
        modelAndView.setViewName("admin-users-all");
        modelAndView.addObject("title", "All Users");
        modelAndView.addObject("userModels", userViewModels);
        if (model.containsAttribute("banned")) {
            modelAndView.addObject("banned");
        }
        if (model.containsAttribute("restored")) {
            modelAndView.addObject("restored");
        }

        return modelAndView;
    }

    @GetMapping("/{username}")
    public ModelAndView userProfile(@PathVariable("username") String username, ModelAndView modelAndView, Model model) {
        modelAndView.setViewName("admin-users-profile");
        UserServiceModel userServiceModel = this.userService.getUserServiceModelByUsername(username);
        modelAndView.addObject("title", String.format(PROFILE_PAGE_TITLE_FORMAT, username));
        modelAndView.addObject("deletedOn", userServiceModel.getDeletedOn());

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

    @PostMapping("/{username}/edit-data")
    public ModelAndView editProfileData(@PathVariable("username") String username, @Valid @ModelAttribute(name = "userDataInput") UserEditDataBindingModel userEditDataBindingModel, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDataInput", bindingResult);
            redirectAttributes.addFlashAttribute("userDataInput", userEditDataBindingModel);
        } else {
            this.userService.editUserDataByUsername(userEditDataBindingModel, username);
            redirectAttributes.addFlashAttribute("done", "done");
        }
        modelAndView.setViewName("redirect:../" + username);

        return modelAndView;
    }

    @PostMapping("/{username}/edit-password")
    public ModelAndView editProfilePassword(@PathVariable("username") String username, @Valid @ModelAttribute(name = "userPasswordInput") UserEditPasswordBindingModel userEditPasswordBindingModel, BindingResult bindingResult, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordInput", bindingResult);
            redirectAttributes.addFlashAttribute("userPasswordInput", userEditPasswordBindingModel);
        } else {
            try {
                this.userService.editUserPasswordByUsername(userEditPasswordBindingModel, username);
                redirectAttributes.addFlashAttribute("done", "done");
            } catch (PasswordsMismatchException e) {
                redirectAttributes.addFlashAttribute("passwordError", "error");
            }
        }
        modelAndView.setViewName("redirect:../" + username);

        return modelAndView;
    }

    @GetMapping("/{username}/ban")
    public ModelAndView banUser(@PathVariable("username") String username, ModelAndView modelAndView) throws UserAlreadyBannedException {
        UserViewModel userViewModel = this.mapper.map(this.userService.getNonDeletedUserServiceModelByUsername(username), UserViewModel.class);
        modelAndView.setViewName("admin-users-ban");
        modelAndView.addObject("title", String.format(PROFILE_BAN_TITLE_FORMAT, username));
        modelAndView.addObject("user", userViewModel);

        return modelAndView;
    }

    @PostMapping("/{username}/ban")
    public ModelAndView banUserConfirm(@PathVariable("username") String username, ModelAndView modelAndView, RedirectAttributes redirectAttributes) throws UserAlreadyBannedException {
        this.userService.banUser(username);
        redirectAttributes.addFlashAttribute("banned", "banned");
        modelAndView.setViewName("redirect:../all");

        return modelAndView;
    }

    @GetMapping("/{username}/restore")
    public ModelAndView restoreUser(@PathVariable("username") String username, ModelAndView modelAndView) throws UserNotBannedException {
        UserViewModel userViewModel = this.mapper.map(this.userService.getDeletedUserByUsername(username), UserViewModel.class);
        modelAndView.setViewName("admin-users-restore");
        modelAndView.addObject("title", String.format(PROFILE_RESTORE_TITLE_FORMAT, username));
        modelAndView.addObject("user", userViewModel);

        return modelAndView;
    }

    @PostMapping("/{username}/restore")
    public ModelAndView restoreUserConfirm(@PathVariable("username") String username, ModelAndView modelAndView, RedirectAttributes redirectAttributes) throws UserNotBannedException {
        this.userService.restoreUser(username);
        redirectAttributes.addFlashAttribute("restored", "restored");
        modelAndView.setViewName("redirect:../all");

        return modelAndView;
    }
}
