package com.blogger.backend.exception;

import java.util.stream.Collectors;

import com.blogger.backend.shared.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorHandler {
    private static final Logger logger = LogManager.getLogger(ErrorHandler.class);

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            NotUniqueEmailException.class,
            NotUniqueUsernameException.class,
            UserNotFoundException.class,
            AuthenticationException.class,
            GeneralErrorException.class,
            AccessDeniedException.class
    })
    ResponseEntity<ApiError> handleCustomException(Exception exception, HttpServletRequest request) {
        ApiError error = new ApiError();
        error.setPath(request.getRequestURI());
        error.setMessage(exception.getMessage());
        logger.error("Error: {}, Path: {}", exception.getMessage(), request.getRequestURI());
        if (exception instanceof MethodArgumentNotValidException) {
            String message = Messages.getMessageForLocale("blogger.validation", LocaleContextHolder.getLocale());
            error.setMessage(message);
            error.setStatus(400);
            var validationErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()
                    .stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                            (existing, replacing) -> existing));
            error.setValidationErrors(validationErrors);
        } else if (exception instanceof GeneralErrorException) {
            error.setStatus(400);
        } else if (exception instanceof NotUniqueEmailException) {
            error.setStatus(400);
            error.setValidationErrors(((NotUniqueEmailException) exception).getValidationErrors());
        } else if (exception instanceof NotUniqueUsernameException) {
            error.setStatus(400);
            error.setValidationErrors(((NotUniqueUsernameException) exception).getValidationErrors());
        } else if (exception instanceof UserNotFoundException) {
            error.setStatus(404);
        } else if (exception instanceof AuthenticationException) {
            error.setStatus(401);
        } else if (exception instanceof AccessDeniedException) {
            error.setStatus(403);
        }

        return ResponseEntity.status(error.getStatus()).body(error);
    }
}