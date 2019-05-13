package edu.tamu.cap.controller.handler;

import static edu.tamu.weaver.response.ApiStatus.ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.exceptions.OntModelReadException;
import edu.tamu.weaver.response.ApiResponse;

@RestController
@ControllerAdvice
public class SchemaControllerAdvice {

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(OntModelReadException.class)
    public ApiResponse handleFindPropertiesByNamespaceHttpException(OntModelReadException e) {
        return new ApiResponse(ERROR, e.getMessage());
    }

}
