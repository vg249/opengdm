package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(GobiiException.class)
    public ResponseEntity GobiiExceptionHandler(GobiiException gEx) {
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError(gEx.getMessage());
        switch (gEx.getGobiiValidationStatusType()) {
            case ENTITY_DOES_NOT_EXIST: {
                errorStatus = HttpStatus.NOT_FOUND;
                break;
            }
            case ENTITY_ALREADY_EXISTS: {
                errorStatus = HttpStatus.CONFLICT;
            }
        }
        return ResponseEntity.status(errorStatus).body(errorPayload);
    }

}
