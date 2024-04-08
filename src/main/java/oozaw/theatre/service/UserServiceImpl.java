package oozaw.theatre.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.UpdateUserDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.UserResponse;
import oozaw.theatre.repository.UserRepository;
import oozaw.theatre.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ValidationService validationService;

   @Transactional
   @Override
   public User create(CreateUserDto createUserDto) {
      validationService.validate(createUserDto);

      if (userRepository.existsByEmail(createUserDto.getEmail())) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
      }

      if (userRepository.existsByPhone(createUserDto.getPhone())) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already registered");
      }

      User user = new User();
      user.setId(UUID.randomUUID().toString());
      user.setName(createUserDto.getName());
      user.setPhone(createUserDto.getPhone());
      user.setEmail(createUserDto.getEmail());
      user.setPassword(BCrypt.hashpw(createUserDto.getPassword(), BCrypt.gensalt()));
      user.setCreatedAt(LocalDateTime.now());
      user.setUpdatedAt(LocalDateTime.now());

      userRepository.save(user);

      return user;
   }

   @Transactional
   @Override
   public UserResponse get(String userId) {
      if (userId == null || userId.isEmpty() || userId.isBlank()) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
      }

      User user = userRepository.findById(userId).orElseThrow(
         () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
      );

      return UserResponse.fromEntity(user);
   }

   @Transactional
   @Override
   public List<UserResponse> getMany(String name) {
      List<User> users;

      if (name != null && !name.isEmpty() && !name.isBlank()) {
         users = userRepository.findByNameLike(name);
      } else {
         users = userRepository.findAll();
      }

      if (users.isEmpty()) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
      }

      return users.stream().map(UserResponse::fromEntity).toList();
   }

   @Override
   public UserResponse update(UpdateUserDto updateUserDto) {
      return null;
   }

   @Override
   public void delete(String userId) {

   }
}
