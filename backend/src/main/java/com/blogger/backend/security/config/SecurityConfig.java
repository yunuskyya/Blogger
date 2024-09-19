package com.blogger.backend.security.config;

import com.blogger.backend.security.token.TokenFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
public class SecurityConfig {
    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("http building...", http);
        http.authorizeHttpRequests((authentication) -> authentication
                .anyRequest().permitAll());

        http.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(new AuthEntryPoint()));
        http.csrf(crsf -> crsf.disable());
        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives("object-src 'none'; frame-ancestors 'self'")));
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}


