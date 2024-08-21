package com.blogger.backend.shared;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    public static String getMessageForLocale(String key, Locale locale) {
        return ResourceBundle.getBundle("messages", locale).getString(key);
    }

    public static String getMessageForLocale(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessageForLocale(key, locale, args);
    }
}
