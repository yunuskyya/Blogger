package com.blogger.backend.exception;

import com.blogger.backend.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GeneralErrorException extends RuntimeException {
    public GeneralErrorException(String messageKey) {
        super(Messages.getMessageForLocale(messageKey, LocaleContextHolder.getLocale()));
    }
}

