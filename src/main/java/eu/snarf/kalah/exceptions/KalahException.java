package eu.snarf.kalah.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Frans on 3/19/2018.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class KalahException extends RuntimeException {

    public KalahException(final String message) {
        super(message);
    }

    public KalahException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
