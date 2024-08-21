package com.blogger.backend.exception;

import java.util.Collections;
import java.util.Map;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;


public class NotUniqueEmailException extends RuntimeException {
    public NotUniqueEmailException() {
        super(Messages.getMessageForLocale("blogger.validation.notunique.email", LocaleContextHolder.getLocale()));
    }

    public Map<String, String> getValidationErrors() {
        return Collections.singletonMap("email", Messages.getMessageForLocale("blogger.validation.notunique.email",
                LocaleContextHolder.getLocale()));
    }
}

