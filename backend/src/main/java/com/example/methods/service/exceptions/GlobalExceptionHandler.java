package com.example.methods.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoRootFoundException.class)
    public ResponseEntity<String> handleNoRootFoundException(NoRootFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InitialPointsNotDeterminedException.class)
    public ResponseEntity<String> handleInitialPointsNotDeterminedException(InitialPointsNotDeterminedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidExpressionException.class)
    public ResponseEntity<String> handleInvalidExpressionException(InvalidExpressionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidInterpolationDataException.class)
    public ResponseEntity<String> handleInvalidInterpolationDataException(InvalidInterpolationDataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ZeroPivotException.class)
    public ResponseEntity<String> handleZeroPivotException(ZeroPivotException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotDiagonalDominanceException.class)
    public ResponseEntity<String> handleNotDiagonalDominanceException(NotDiagonalDominanceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
