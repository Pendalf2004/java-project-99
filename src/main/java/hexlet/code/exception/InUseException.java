package hexlet.code.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class InUseException extends RuntimeException {
    public InUseException(String message) {
        super(message);
    }
}
