package edu.tamu.cap.controller.handler;

import static edu.tamu.weaver.response.ApiStatus.ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.exceptions.InvalidTripleException;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@ControllerAdvice
public class InvalidTripleAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(InvalidTripleException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleInvalidLiteralException(InvalidTripleException exception) {
        String message = exception.getMessage().length() == 0 ? "Triple is invalid." : exception.getMessage();
        logger.debug(message, exception);
        return new ApiResponse(ERROR, message);
    }

}
