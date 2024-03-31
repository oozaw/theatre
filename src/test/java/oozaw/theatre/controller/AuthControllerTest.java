package oozaw.theatre.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.dto.LogoutDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.repository.UserRepository;
import oozaw.theatre.security.BCrypt;
import oozaw.theatre.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test Name");
        createUserDto.setEmail("test@example.com");
        createUserDto.setPhone("083294324893");
        createUserDto.setPassword("test_password");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getEmail());
            assertEquals(createUserDto.getEmail(), response.getData().getEmail());

            User userDB = userRepository.findById(response.getData().getId()).orElse(null);
            assertNotNull(userDB);
            assertEquals(userDB.getToken(), response.getData().getToken());
            assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("");
        createUserDto.setEmail("");
        createUserDto.setPhone("");
        createUserDto.setPassword("");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());

            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {

        User user = new User();
        user.setId("testId");
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setPhone("083294324893");
        user.setPassword("test_password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test Name");
        createUserDto.setEmail("test@example.com");
        createUserDto.setPhone("083294324893");
        createUserDto.setPassword("test_password");

        mockMvc.perform(
                post("/api/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginSuccess() throws Exception {
        // insert a user
        User user = new User();
        user.setId("testId");
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setPhone("083294324893");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("test_password");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());
            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getEmail());
            assertEquals(loginDto.getEmail(), response.getData().getEmail());

            User userDB = userRepository.findById(response.getData().getId()).orElse(null);
            assertNotNull(userDB);
            assertEquals(userDB.getToken(), response.getData().getToken());
            assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
        });
    }

    @Test
    void testLoginUnauthorized() throws Exception {
        // insert a user
        User user = new User();
        user.setId("testId");
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setPhone("083294324893");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test2@example.com");
        loginDto.setPassword("test_password");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());

            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }

    @Test
    void testLoginBadRequest() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("");
        loginDto.setPassword("");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());

            WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
            assertNull(response.getData());
        });
    }

    @Test
    void testLogoutSuccess() throws Exception {
        // insert a user
        User user = new User();
        user.setId("testId");
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setPhone("083294324893");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));
        user.setToken("test_token");
        user.setTokenExpiredAt(System.currentTimeMillis());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        LogoutDto logoutDto = new LogoutDto();
        logoutDto.setUserId("testId");

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutDto))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());

            User userDB = userRepository.findById(logoutDto.getUserId()).orElse(null);

            assertNotNull(userDB);
            assertEquals(userDB.getId(), user.getId());
            assertNull(userDB.getToken());
            assertNull(userDB.getTokenExpiredAt());
        });
    }

    @Test
    void testLogoutBadRequest() throws Exception {
        // insert a user
        User user = new User();
        user.setId("testId");
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setPhone("083294324893");
        user.setPassword(BCrypt.hashpw("test_password", BCrypt.gensalt()));
        user.setToken("test_token");
        user.setTokenExpiredAt(System.currentTimeMillis());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        LogoutDto logoutDto = new LogoutDto();
        logoutDto.setUserId("");

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutDto))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {

            log.info(result.getResponse().getContentAsString());

            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }
}