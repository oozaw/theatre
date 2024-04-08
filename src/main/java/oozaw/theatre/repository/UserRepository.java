package oozaw.theatre.repository;

import oozaw.theatre.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
   boolean existsByEmail(String email);

   Optional<User> findByEmail(String email);

   boolean existsByPhone(String phone);

   @Query("select u from User u where u.name like %?1%")
   List<User> findByNameLike(String name);

   Optional<User> findFirstByToken(String token);
}
