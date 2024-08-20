package com.blogger.backend.config;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Bu sınıf, kullanıcının kimlik doğrulaması başarısız olduğunda çalışır ve kullanıcıya kimlik doğrulaması hatası mesajını gönderir.
 * Bu sınıf, AuthenticationEntryPoint interface'i uygular.
 * AuthenticationEntryPoint interface'i, kimlik doğrulaması başarısız olduğunda çalışacak özelleştirilmiş bir işlemi tanımlamak için kullanılır.
 * commence() metodu, kimlik doğrulaması başarısız olduğunda çalışacak işlemi tanımlar.
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LogManager.getLogger(AuthEntryPoint.class);

    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        try {
            logger.debug("commence running...");
            logger.debug("Authentication error: {}", authException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication error");
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            logger.warn("Exception occurred while handling authentication error", e);
            resolver.resolveException(request, response, null, authException);
        }
    }
}

