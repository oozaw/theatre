package oozaw.theatre.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oozaw.theatre.entity.Movie;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {

   private String id;

   private String title;

   private String synopsys;

   private String duration;

   private LocalDateTime createdAt;

   private LocalDateTime updatedAt;

   public static MovieResponse fromEntity(Movie movie) {
      return MovieResponse.builder()
         .id(movie.getId())
         .title(movie.getTitle())
         .synopsys(movie.getSynopsys())
         .duration(movie.getDuration())
         .createdAt(movie.getCreatedAt())
         .updatedAt(movie.getUpdatedAt())
         .build();
   }
}
