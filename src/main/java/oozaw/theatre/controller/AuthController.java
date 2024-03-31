package oozaw.theatre.controller;

import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.dto.LogoutDto;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "/api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<AuthResponse>> register(@RequestBody CreateUserDto createUserDto) {
        WebResponse<AuthResponse> body = WebResponse.<AuthResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED.name())
                .data(authService.register(createUserDto))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<AuthResponse>> login(@RequestBody LoginDto loginDto) {
        WebResponse<AuthResponse> body = WebResponse.<AuthResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(authService.login(loginDto))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> logout(@RequestBody LogoutDto logoutDto) {
        authService.logout(logoutDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                WebResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .status(HttpStatus.OK.name())
                        .build()
        );
    }
}
