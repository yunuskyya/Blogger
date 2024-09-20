package com.blogger.backend.service.Impl;

import com.blogger.backend.dto.request.RegisterUserRequest;
import com.blogger.backend.dto.response.GetAllUserResponse;
import com.blogger.backend.dto.response.GetUserByIdResponse;
import com.blogger.backend.exception.UserNotFoundException;
import com.blogger.backend.model.User;
import com.blogger.backend.model.enums.Role;
import com.blogger.backend.repository.UserRepository;
import com.blogger.backend.service.MailService;
import com.blogger.backend.service.UserService;
import com.blogger.backend.shared.Messages;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserService userService;

    private MailService mailService;

    private UserRepository userRepository;

    private ModelMapper modelMapperForResponse;

    private ModelMapper modelMapperForRequest;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mailService = Mockito.mock(MailService.class);
        userRepository = Mockito.mock(UserRepository.class);
        modelMapperForResponse = Mockito.mock(ModelMapper.class);
        modelMapperForRequest = Mockito.mock(ModelMapper.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(mailService, userRepository, modelMapperForResponse, modelMapperForRequest, passwordEncoder);
    }

    //1- test isminin yazılması
    @Test
    @DisplayName("Should save user and send activation email when valid RegisterUserRequest is provided")
    void shouldSaveUserAndSendActivationEmailWhenRegisterUserRequestIsValid() {
        // 2: Test verilerinin oluşturulması
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("test@gmail.com");
        // Aktivasyon tokeni, yetkiler ve aktiflik durumu ayarlandı
        user.setActivationToken(UUID.randomUUID().toString());
        user.setAuthorities(new HashSet<>(Collections.singletonList(Role.ROLE_USER)));
        user.setActive(false);
        user.setPassword("encodedPassword");

        // 3- mocklanması gereken unit dışı bağımlılıkların davranışının belirlenmesi
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "John", "Doe", "12345678901", "johndoe", "test@gmail.com", "Password123");

        Mockito.when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(modelMapperForRequest.map(registerUserRequest, User.class)).thenReturn(user);
        Mockito.doNothing().when(mailService).sendAccountActivationEmail(user.getEmail(), user.getActivationToken());
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);

        // 4-: test metodunu çalıştırmak
        userService.registerUser(registerUserRequest);

        // 5: beklenen sonuçları karşılaştır
        Mockito.verify(mailService, times(1)).sendAccountActivationEmail(Mockito.anyString(), Mockito.anyString());
        Assertions.assertEquals(user, userRepository.saveAndFlush(user));
    }

    @Test
    @DisplayName("Should not save user and should not send activation email when email is already in use")
    void shouldNotSaveUserAndSendActivationEmailWhenEmailIsAlreadyInUse() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "John", "Doe", "12345678901", "johndoe", "test@gmail.com", "Password123");

        User existingUser = new User();
        existingUser.setEmail("test@gmail.com");

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(Optional.of(existingUser));

        Messages.getMessageForLocale("blogger.validation.notunique.email", LocaleContextHolder.getLocale());

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(mailService, never()).sendAccountActivationEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("Should rollback transaction when MailException is thrown during email sending")
    void shouldRollbackTransactionWhenMailExceptionIsThrown(){
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(
                "John", "Doe", "12345678901", "johndoe", "test@gmail.com", "Password123");

        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("test@gmail.com");
        user.setActivationToken("randomToken");
        user.setPassword("encodedPassword");

        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn("encodedPassword");
        when(modelMapperForRequest.map(registerUserRequest, User.class)).thenReturn(user);

        verify(userRepository, never()).saveAndFlush(user);
    }
    @Test
    void getAllUser(){
        Pageable pageable = PageRequest.of(0, 10);

        User user1 = new User();
        user1.setUsername("johndoe");
        User user2 = new User();
        user2.setUsername("janedoe");

        List<User> userList = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        GetAllUserResponse response1 = new GetAllUserResponse();
        response1.setUsername("johndoe");
        GetAllUserResponse response2 = new GetAllUserResponse();
        response2.setUsername("janedoe");

        List<GetAllUserResponse> responseList = Arrays.asList(response1, response2);
        Page<GetAllUserResponse> responsePage = new PageImpl<>(responseList, pageable, responseList.size());

        Mockito.when(userRepository.findAll(pageable)).thenReturn(userPage);
        Mockito.when(modelMapperForResponse.map(user1, GetAllUserResponse.class)).thenReturn(response1);
        Mockito.when(modelMapperForResponse.map(user2, GetAllUserResponse.class)).thenReturn(response2);


        Page<GetAllUserResponse> result = userService.getAllUsers(pageable);


        Assertions.assertEquals(responsePage.getContent().get(0).getUsername(),
                result.getContent().get(0).getUsername());
        Assertions.assertEquals(responsePage.getContent().get(1).getUsername(),
                result.getContent().get(1).getUsername());


        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(modelMapperForResponse, Mockito.times(2)).map(Mockito.any(User.class), Mockito.eq(GetAllUserResponse.class));

    }

    @Test
    void getUserByıd(){

        int userId= 1;
        User user = new User();
        user.setId(userId);
        user.setUsername("yunus");

        GetUserByIdResponse expectedResponse = new GetUserByIdResponse();
        expectedResponse.setUsername("yunus");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapperForResponse.map(user,GetUserByIdResponse.class)).thenReturn(expectedResponse);


        GetUserByIdResponse actualResponse = new GetUserByIdResponse();

        Assertions.assertEquals(expectedResponse.getUsername(),actualResponse.getUsername());
        verify(userRepository,times(1)).findById(userId);

    }
    @Test
    void testGetUserById_UserNotFound() {
        String exceptedMessage = "Kullanıcı bulunamadı. 1";

        String actualMessage = Messages.getMessageForLocale(
                "blogger.user.notfound.error.message",LocaleContextHolder.getLocale(),1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1);
        });

        Assertions.assertEquals(exceptedMessage,actualMessage);
    }




        @AfterEach
    void tearDown() {
    }
}
