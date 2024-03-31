package oozaw.theatre.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest {
    private static final String API_USERS = "/api/users";

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
    void testCreateUserSuccess() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test Name");
        createUserDto.setEmail("test@example.com");
        createUserDto.setPhone("083294324893");
        createUserDto.setPassword("test_password");

        mockMvc.perform(
                post(API_USERS)
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
            assertNotNull(response.getData().getEmail());
            assertEquals(createUserDto.getEmail(), response.getData().getEmail());

            User userDB = userRepository.findById(response.getData().getId()).orElse(null);
            assertNotNull(userDB);
        });
    }

    @Test
    void testCreateUserBadRequest() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test Name");
        createUserDto.setEmail("");
        createUserDto.setPhone("");
        createUserDto.setPassword("test_password");

        mockMvc.perform(
                post(API_USERS)
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
    void testCreateUserDuplicate() throws Exception {
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
        createUserDto.setPhone("0932428304");
        createUserDto.setPassword("test_password");

        mockMvc.perform(
                post(API_USERS)
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
}