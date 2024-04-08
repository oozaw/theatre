package oozaw.theatre.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.Role;
import oozaw.theatre.model.UserResponse;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

   @Test
   void testGetUserSuccess() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setRole(Role.USER);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/{userId}", user.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData().getEmail());
         assertEquals(user.getEmail(), response.getData().getEmail());
      });
   }

   @Test
   void testGetUserBadRequest() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/{userId}", "  ")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
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
   void testGetUserNotFound() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/{userId}", "testIdNotFound")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }

   @Test
   void testGetCurrentUserSuccess() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setRole(Role.USER);
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<AuthResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData().getEmail());
         assertEquals(user.getEmail(), response.getData().getEmail());
      });
   }

   @Test
   void testGetCurrentUserUnauthorizedNoApi() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
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
   void testGetCurrentUserUnauthorizedApiNotFound() throws Exception {

      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324893");
      user.setPassword("test_password");
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      mockMvc.perform(
         get("/api/users/current")
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "another_token")
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
   void testGetManyUsersSuccess() throws Exception {
      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324891");
      user.setPassword("test_password");
      user.setRole(Role.USER);
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      User user2 = new User();
      user2.setId("testId2");
      user2.setName("Test Name");
      user2.setEmail("test2@example.com");
      user2.setPhone("083294324892");
      user2.setPassword("test_password");
      user2.setRole(Role.USER);
      user2.setToken("test_token2");
      user2.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user2.setCreatedAt(LocalDateTime.now());
      user2.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user2);

      User user3 = new User();
      user3.setId("testId3");
      user3.setName("Test Name");
      user3.setEmail("test3@example.com");
      user3.setPhone("083294324893");
      user3.setPassword("test_password");
      user3.setRole(Role.USER);
      user3.setToken("test_token3");
      user3.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user3.setCreatedAt(LocalDateTime.now());
      user3.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user3);

      mockMvc.perform(
         get("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token2")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
//         assertEquals(user.getEmail(), response.getData().getEmail());
      });
   }

   @Test
   void testGetManyUsersByNameSuccess() throws Exception {
      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324891");
      user.setPassword("test_password");
      user.setRole(Role.USER);
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      User user2 = new User();
      user2.setId("testId2");
      user2.setName("Test Name");
      user2.setEmail("test2@example.com");
      user2.setPhone("083294324892");
      user2.setPassword("test_password");
      user2.setRole(Role.USER);
      user2.setToken("test_token2");
      user2.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user2.setCreatedAt(LocalDateTime.now());
      user2.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user2);

      User user3 = new User();
      user3.setId("testId3");
      user3.setName("Test Name");
      user3.setEmail("test3@example.com");
      user3.setPhone("083294324893");
      user3.setPassword("test_password");
      user3.setRole(Role.USER);
      user3.setToken("test_token3");
      user3.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user3.setCreatedAt(LocalDateTime.now());
      user3.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user3);

      User user4 = new User();
      user4.setId("testId4");
      user4.setName("Different Name");
      user4.setEmail("test4@example.com");
      user4.setPhone("083294324894");
      user4.setPassword("test_password");
      user4.setRole(Role.USER);
      user4.setToken("test_token4");
      user4.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user4.setCreatedAt(LocalDateTime.now());
      user4.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user4);

      User user5 = new User();
      user5.setId("testId5");
      user5.setName("Different Name");
      user5.setEmail("test5@example.com");
      user5.setPhone("083294324895");
      user5.setPassword("test_password");
      user5.setRole(Role.USER);
      user5.setToken("test_token5");
      user5.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user5.setCreatedAt(LocalDateTime.now());
      user5.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user5);

      mockMvc.perform(
         get("/api/users?name={name}", "Different")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token2")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertNotEquals(0, response.getData().size());
         assertEquals(3, response.getData().size());
      });
   }

   @Test
   void testGetManyUsersByNameNotFoundEmpty() throws Exception {
      User user = new User();
      user.setId("testId");
      user.setName("Test Name");
      user.setEmail("test@example.com");
      user.setPhone("083294324891");
      user.setPassword("test_password");
      user.setRole(Role.USER);
      user.setToken("test_token");
      user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user);

      User user2 = new User();
      user2.setId("testId2");
      user2.setName("Test Name");
      user2.setEmail("test2@example.com");
      user2.setPhone("083294324892");
      user2.setPassword("test_password");
      user2.setRole(Role.USER);
      user2.setToken("test_token2");
      user2.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user2.setCreatedAt(LocalDateTime.now());
      user2.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user2);

      User user3 = new User();
      user3.setId("testId3");
      user3.setName("Test Name");
      user3.setEmail("test3@example.com");
      user3.setPhone("083294324893");
      user3.setPassword("test_password");
      user3.setRole(Role.USER);
      user3.setToken("test_token3");
      user3.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user3.setCreatedAt(LocalDateTime.now());
      user3.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user3);

      User user4 = new User();
      user4.setId("testId4");
      user4.setName("Different Name");
      user4.setEmail("test4@example.com");
      user4.setPhone("083294324894");
      user4.setPassword("test_password");
      user4.setRole(Role.USER);
      user4.setToken("test_token4");
      user4.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user4.setCreatedAt(LocalDateTime.now());
      user4.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user4);

      User user5 = new User();
      user5.setId("testId5");
      user5.setName("Different Name");
      user5.setEmail("test5@example.com");
      user5.setPhone("083294324895");
      user5.setPassword("test_password");
      user5.setRole(Role.USER);
      user5.setToken("test_token5");
      user5.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
      user5.setCreatedAt(LocalDateTime.now());
      user5.setUpdatedAt(LocalDateTime.now());
      userRepository.save(user5);

      mockMvc.perform(
         get("/api/users?name={name}", "koko")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token2")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {

         log.info(result.getResponse().getContentAsString());
         WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }
}