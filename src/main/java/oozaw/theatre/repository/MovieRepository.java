package oozaw.theatre.repository;

import oozaw.theatre.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {
   @Query("select m from Movie m where upper(m.title) like upper(concat('%', ?1, '%'))")
   List<Movie> findByTitleContainsIgnoreCase(@Nullable String title);
}
