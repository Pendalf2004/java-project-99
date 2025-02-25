package hexlet.code.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InUseException extends RuntimeException {
    public InUseException(String message) {
        super(message);
    }
}
