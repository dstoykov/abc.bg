package dst.abc_bg.areas.error.handlers;

import dst.abc_bg.areas.user.exceptions.UserAlreadyBannedException;
import dst.abc_bg.areas.user.exceptions.UserAlreadyExistsException;
import dst.abc_bg.areas.user.exceptions.UserNotBannedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        redirectAttributes.addFlashAttribute("userExistsError", "error");
        modelAndView.setViewName("redirect:register");

        return modelAndView;
    }

    @ExceptionHandler(MessagingException.class)
    public ModelAndView handleMessagingException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:../errors/error");
        return modelAndView;
    }
}