package oozaw.theatre.controller;

import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.WebResponse;
import oozaw.theatre.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public WebResponse<AuthResponse> login(@RequestBody LoginDto loginDto) {
        return WebResponse.<AuthResponse>builder()
                .code(200)
                .status("OK")
                .data(authService.login(loginDto))
                .build();
    }
}
