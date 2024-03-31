package oozaw.theatre.controller;

import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.UserResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

   @Autowired
   private UserService userService;

   @PostMapping(
      path = "/api/users",
      consumes = "application/json",
      produces = "application/json"
   )
   public ResponseEntity<WebResponse<UserResponse>> createUser(@RequestBody CreateUserDto createUserDto) {
      User user = userService.create(createUserDto);

      return ResponseEntity.status(HttpStatus.CREATED).body(
         WebResponse.<UserResponse>builder()
            .code(HttpStatus.CREATED.value())
            .status(HttpStatus.CREATED.name())
            .data(UserResponse.fromEntity(user))
            .build()
      );
   }

   @GetMapping(
      path = "/api/users/{userId}",
      produces = "application/json"
   )
   public ResponseEntity<WebResponse<UserResponse>> getUser(@PathVariable String userId) {
      UserResponse userResponse = userService.get(userId);

      return ResponseEntity.status(HttpStatus.OK).body(
         WebResponse.<UserResponse>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK.name())
            .data(userResponse)
            .build()
      );
   }
}
