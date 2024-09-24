package com.blogger.backend.service.Impl;
import com.blogger.backend.config.ModelMapperConfig;
import com.blogger.backend.dto.request.CredentialsRequest;
import com.blogger.backend.dto.response.AuthResponse;
import com.blogger.backend.dto.response.GetUserByEmailResponse;
import com.blogger.backend.exception.AuthenticationException;
import com.blogger.backend.model.Token;
import com.blogger.backend.model.User;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.shared.Messages;
import com.blogger.backend.security.token.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImpTest {

    @InjectMocks
    private AuthServiceImp authService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapperConfig modelMapperConfig;

    @Mock
    private ModelMapper modelMapperForResponse;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(new Locale("tr", "TR"));  // Testler için Türkçe yerelleştirme ayarlandı
        closeable = MockitoAnnotations.openMocks(this);
        when(modelMapperConfig.modelMapperForResponse()).thenReturn(modelMapperForResponse);

    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenAuthResponseIsReturned() {
        CredentialsRequest credentials = new CredentialsRequest("test@example.com", "validPass", null, null);

        User user = createValidUser();

        GetUserByEmailResponse userResp = createGetUserByEmailResponse(user);

        when(userRepository.findByEmail(credentials.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(credentials.password(), user.getPassword())).thenReturn(true);
        when(modelMapperConfig.modelMapperForResponse().map(user, GetUserByEmailResponse.class)).thenReturn(userResp);

        Token token = createValidToken(user);

        when(tokenService.generateToken(any(GetUserByEmailResponse.class), any(CredentialsRequest.class))).thenReturn(token);

        AuthResponse authResponse = authService.authenticate(credentials);

        assertNotNull(authResponse);
        assertEquals("dummyToken", authResponse.getToken().getTokenId());
        assertEquals("Bearer", authResponse.getToken().getPrefix());
        assertEquals("test@example.com", authResponse.getUser().getEmail());
        assertEquals("validUser", authResponse.getUser().getUsername());

        verify(userRepository, times(1)).findByEmail(credentials.email());
        verify(passwordEncoder, times(1)).matches(credentials.password(), user.getPassword());
        verify(tokenService, times(1)).generateToken(userResp, credentials);
    }

    @Test
    void givenInvalidPassword_whenAuthenticate_thenAuthenticationExceptionIsThrown() {
        CredentialsRequest credentials = new CredentialsRequest("test@example.com", "invalidPass", null, null);

        User user = createValidUser();

        Mockito.when(userRepository.findByEmail(credentials.email())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(credentials.password(), user.getPassword())).thenReturn(false);

        String expectedMessage = Messages.getMessageForLocale("blogger.authentication.error.message", LocaleContextHolder.getLocale());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.authenticate(credentials));
        assertEquals(expectedMessage, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(credentials.email());
        verify(passwordEncoder, times(1)).matches(credentials.password(), user.getPassword());
    }

    @Test
    void givenInactiveUser_whenAuthenticate_thenAuthenticationExceptionIsThrown() {
        // Given: Pasif durumdaki bir kullanıcı
        CredentialsRequest credentials = new CredentialsRequest("test@example.com", "validPass", null, null);

        User user = createValidUser();
        user.setActive(false);  // Kullanıcı pasif

        when(userRepository.findByEmail(credentials.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(credentials.password(), user.getPassword())).thenReturn(true);

        String expectedMessage = Messages.getMessageForLocale("blogger.authentication.inactive.message", LocaleContextHolder.getLocale());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.authenticate(credentials));
        assertEquals(expectedMessage, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(credentials.email());
        verify(passwordEncoder, times(1)).matches(credentials.password(), user.getPassword());
    }

    @Test
    void givenDeletedUser_whenAuthenticate_thenAuthenticationExceptionIsThrown() {
        CredentialsRequest credentials = new CredentialsRequest("test@example.com", "validPass", null, null);

        User user = createValidUser();
        user.setDeleted(true);

        when(userRepository.findByEmail(credentials.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(credentials.password(), user.getPassword())).thenReturn(true);

        String expectedMessage = Messages.getMessageForLocale("blogger.authentication.deleted.message", LocaleContextHolder.getLocale());

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.authenticate(credentials));
        assertEquals(expectedMessage, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(credentials.email());
        verify(passwordEncoder, times(1)).matches(credentials.password(), user.getPassword());
    }

    @Test
    void givenValidAuthorizationHeader_whenLogout_thenNoExceptionIsThrown() {
        String authorizationHeader = "Bearer dummyToken";

        assertDoesNotThrow(() -> authService.logout(authorizationHeader));

        verify(tokenService, times(1)).logout(authorizationHeader);
    }

    @Test
    void givenInvalidAuthorizationHeader_whenLogout_thenExceptionIsThrown() {
        String authorizationHeader = "InvalidToken";

        doThrow(new RuntimeException("Invalid token")).when(tokenService).logout(authorizationHeader);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.logout(authorizationHeader));
        assertEquals("Invalid token", exception.getMessage());

        verify(tokenService, times(1)).logout(authorizationHeader);
    }

    // Yardımcı metodlar
    private User createValidUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("validUser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setActive(true);
        user.setDeleted(false);
        return user;
    }

    private GetUserByEmailResponse createGetUserByEmailResponse(User user) {
        GetUserByEmailResponse userResp = new GetUserByEmailResponse();
        userResp.setId(user.getId());
        userResp.setUsername(user.getUsername());
        userResp.setPassword(user.getPassword());
        userResp.setEmail(user.getEmail());
        userResp.setActive(user.isActive());
        userResp.setDeleted(user.isDeleted());
        return userResp;
    }

    private Token createValidToken(User user) {
        Token token = new Token();
        token.setTokenId("dummyToken");
        token.setExpirationDate(System.currentTimeMillis() + 10000); // Token geçerli
        token.setActive(true);
        token.setUser(user);
        return token;
    }
}
