package oozaw.theatre.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.entity.Theatre;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.Role;
import oozaw.theatre.model.TheatreResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.repository.TheatreRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class TheatreControllerTest {

   private final static String API_THEATRES = "/api/theatres";

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private TheatreRepository theatreRepository;

   @Autowired
   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      userRepository.deleteAll();
      theatreRepository.deleteAll();
   }

   void insertData() {
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

      Theatre theatre = new Theatre();
      theatre.setId("testId");
      theatre.setName("Theatre 1");
      theatre.setCity(1);
      theatre.setProvince(1);
      theatre.setAddress("Address1");
      theatre.setLatitude("234234");
      theatre.setLongitude("234234");
      theatre.setCreatedAt(LocalDateTime.now());
      theatre.setUpdatedAt(LocalDateTime.now());

      Theatre theatre2 = new Theatre();
      theatre2.setId("testId2");
      theatre2.setName("Theatre 2");
      theatre2.setCity(1);
      theatre2.setProvince(1);
      theatre2.setAddress("Address2");
      theatre2.setLatitude("234234");
      theatre2.setLongitude("234234");
      theatre2.setCreatedAt(LocalDateTime.now());
      theatre2.setUpdatedAt(LocalDateTime.now());

      Theatre theatre3 = new Theatre();
      theatre3.setId("testId3");
      theatre3.setName("Theatre 3");
      theatre3.setCity(1);
      theatre3.setProvince(1);
      theatre3.setAddress("Address3");
      theatre3.setLatitude("234234");
      theatre3.setLongitude("234234");
      theatre3.setCreatedAt(LocalDateTime.now());
      theatre3.setUpdatedAt(LocalDateTime.now());

      Theatre theatre4 = new Theatre();
      theatre4.setId("testId4");
      theatre4.setName("Theatre 4");
      theatre4.setCity(1);
      theatre4.setProvince(1);
      theatre4.setAddress("Address4");
      theatre4.setLatitude("234234");
      theatre4.setLongitude("234234");
      theatre4.setCreatedAt(LocalDateTime.now());
      theatre4.setUpdatedAt(LocalDateTime.now());

      userRepository.save(user);
      theatreRepository.save(theatre);
      theatreRepository.save(theatre2);
      theatreRepository.save(theatre3);
      theatreRepository.save(theatre4);
   }

   @Test
   void testGetManyTheatresSuccess() throws Exception {
      insertData();

      mockMvc.perform(
         get(API_THEATRES)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<TheatreResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertEquals(4, response.getData().size());
      });
   }

   @Test
   void testGetManyTheatresNotFound() throws Exception {

      mockMvc.perform(
         get(API_THEATRES)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<TheatreResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }

   @Test
   void testGetManyTheatresByNameSuccess() throws Exception {
      insertData();

      mockMvc.perform(
         get(API_THEATRES + "?name={name}", "Theatre 1")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<TheatreResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertEquals(1, response.getData().size());
      });
   }

   @Test
   void testGetManyTheatresByNameNotFound() throws Exception {
      insertData();

      mockMvc.perform(
         get(API_THEATRES + "?name={name}", "Tidak ada test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<TheatreResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }

   @Test
   void testGetDetailTheatreSuccess() throws Exception {
      insertData();

      mockMvc.perform(
         get(API_THEATRES + "/{theatreId}", "testId")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<TheatreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());

         assertEquals(response.getData().getName(), "Theatre 1");
      });
   }

   @Test
   void testGetDetailTheatreNotFound() throws Exception {
      insertData();

      mockMvc.perform(
         get(API_THEATRES + "/{theatreId}", "testIdNotFound")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<TheatreResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }
}