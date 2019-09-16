package dst.abc_bg.areas.user.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class UserNotBannedException extends Exception {
    public UserNotBannedException(String message) {
        super(message);
    }
}
