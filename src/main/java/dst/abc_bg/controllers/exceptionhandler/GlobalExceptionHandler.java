package dst.abc_bg.controllers.exceptionhandler;

import dst.abc_bg.exceptions.UserAlreadyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
