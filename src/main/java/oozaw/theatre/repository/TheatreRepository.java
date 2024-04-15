package oozaw.theatre.repository;

import oozaw.theatre.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TheatreRepository extends JpaRepository<Theatre, String> {
   @Query("select t from Theatre t where t.name like concat('%', ?1, '%') and t.city = ?2 and t.province = ?3")
   List<Theatre> findByNameContainsAndCityAndProvince(String name, int city, int province);

   @Query("select t from Theatre t where upper(t.name) like upper(concat('%', ?1, '%'))")
   List<Theatre> findByNameContainsIgnoreCase(String name);
}
