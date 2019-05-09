package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(GobiiException.class)
    public ResponseEntity GobiiExceptionHandler(GobiiException gEx) {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError(gEx.getMessage());
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (gEx.getGobiiValidationStatusType()) {
            case ENTITY_DOES_NOT_EXIST: {
                errorStatus = HttpStatus.NOT_FOUND;
                break;
            }
            case ENTITY_ALREADY_EXISTS: {
                errorStatus = HttpStatus.CONFLICT;
                break;
            }
            case BAD_REQUEST: {
                errorStatus = HttpStatus.BAD_REQUEST;
            }
        }
        return ResponseEntity.status(errorStatus).body(errorPayload);
    }

}
