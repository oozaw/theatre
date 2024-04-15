package oozaw.theatre.controller;

import oozaw.theatre.model.MovieResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

   @Autowired
   private MovieService movieService;

   @GetMapping(
      path = "",
      produces = MediaType.APPLICATION_JSON_VALUE
   )
   ResponseEntity<WebResponse<List<MovieResponse>>> getMany(@RequestParam(required = false) String title) {
      List<MovieResponse> movies = movieService.getMany(title);

      return ResponseEntity.status(HttpStatus.OK).body(
         WebResponse.<List<MovieResponse>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .data(movies)
            .build()
      );
   }

   @GetMapping(
      value = "/{movieId}",
      produces = MediaType.APPLICATION_JSON_VALUE
   )
   ResponseEntity<WebResponse<MovieResponse>> getDetail(@PathVariable String movieId) {
      return ResponseEntity.status(HttpStatus.OK).body(
         WebResponse.<MovieResponse>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .data(movieService.get(movieId))
            .build()
      );
   }
}
