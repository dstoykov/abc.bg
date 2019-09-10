package dst.abc_bg.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CannotAccessMailException extends Exception {
    public CannotAccessMailException(String message) {
        super(message);
    }
}
