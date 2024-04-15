package oozaw.theatre.service;

import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.entity.Movie;
import oozaw.theatre.model.MovieResponse;
import oozaw.theatre.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

   @Autowired
   private MovieRepository movieRepository;

   @Override
   public List<MovieResponse> getMany(String title) {
      List<Movie> movies;

      if (title != null && !title.isBlank() && !title.isEmpty()) {
         movies = movieRepository.findByTitleContainsIgnoreCase(title);
      } else {
         movies = movieRepository.findAll();
      }

      if (movies.isEmpty()) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No movies found");
      }

      return movies.stream().map(MovieResponse::fromEntity).toList();
   }

   @Override
   public MovieResponse get(String movieId) {
      return movieRepository.findById(movieId).map(MovieResponse::fromEntity)
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
   }
}
