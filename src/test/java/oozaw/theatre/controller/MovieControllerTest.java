package oozaw.theatre.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.entity.Movie;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.MovieResponse;
import oozaw.theatre.model.Role;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.repository.MovieRepository;
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
class MovieControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private MovieRepository movieRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      movieRepository.deleteAll();

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

      Movie movie = new Movie();
      movie.setId("testId");
      movie.setTitle("Title 1");
      movie.setSynopsys("Synopsys 1");
      movie.setDuration("12032");
      movie.setCreatedAt(LocalDateTime.now());
      movie.setUpdatedAt(LocalDateTime.now());

      Movie movie2 = new Movie();
      movie2.setId("testId2");
      movie2.setTitle("Title 2");
      movie2.setSynopsys("Synopsys 2");
      movie2.setDuration("12032");
      movie2.setCreatedAt(LocalDateTime.now());
      movie2.setUpdatedAt(LocalDateTime.now());

      Movie movie3 = new Movie();
      movie3.setId("testId3");
      movie3.setTitle("Title 3");
      movie3.setSynopsys("Synopsys 3");
      movie3.setDuration("12032");
      movie3.setCreatedAt(LocalDateTime.now());
      movie3.setUpdatedAt(LocalDateTime.now());

      Movie movie4 = new Movie();
      movie4.setId("testId4");
      movie4.setTitle("Title 4");
      movie4.setSynopsys("Synopsys 4");
      movie4.setDuration("12032");
      movie4.setCreatedAt(LocalDateTime.now());
      movie4.setUpdatedAt(LocalDateTime.now());

      userRepository.save(user);
      movieRepository.save(movie);
      movieRepository.save(movie2);
      movieRepository.save(movie3);
      movieRepository.save(movie4);
   }

   @Test
   void testGetManyMoviesSuccess() throws Exception {
      mockMvc.perform(
         get("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<MovieResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<MovieResponse>>>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertEquals(4, response.getData().size());
      });
   }

   @Test
   void testGetManyMoviesByTitleSuccess() throws Exception {
      mockMvc.perform(
         get("/api/movies?title={title}", "Title 1")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<MovieResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<MovieResponse>>>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertEquals(1, response.getData().size());
      });
   }

   @Test
   void testGetManyMoviesByTitleNotFound() throws Exception {
      mockMvc.perform(
         get("/api/movies?title={title}", "Not found test")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<List<MovieResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<MovieResponse>>>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }

   @Test
   void testGetDetailMovieSuccess() throws Exception {
      mockMvc.perform(
         get("/api/movies/{movieId}", "testId")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isOk()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<MovieResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<MovieResponse>>() {
         });

         assertNull(response.getErrors());
         assertNotNull(response.getData());
         assertEquals("Title 1", response.getData().getTitle());
      });
   }

   @Test
   void testGetDetailMovieNotFound() throws Exception {
      mockMvc.perform(
         get("/api/movies/{movieId}", "testIdNotFound")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", "test_token")
      ).andExpectAll(
         status().isNotFound()
      ).andDo(result -> {
         log.info(result.getResponse().getContentAsString());

         WebResponse<MovieResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<MovieResponse>>() {
         });

         assertNotNull(response.getErrors());
         assertNull(response.getData());
      });
   }
}