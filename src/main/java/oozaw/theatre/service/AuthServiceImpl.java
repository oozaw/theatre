package oozaw.theatre.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.dto.LogoutDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.AuthResponse;
import oozaw.theatre.model.UserResponse;
import oozaw.theatre.repository.UserRepository;
import oozaw.theatre.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public AuthResponse register(CreateUserDto createUserDto) {

        User user = userService.create(createUserDto);

        generateToken(user);

        userRepository.save(user);

        return AuthResponse.fromEntity(user);
    }

    @Transactional
    @Override
    public AuthResponse login(LoginDto loginDto) {
        validationService.validate(loginDto);

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {

            generateToken(user);

            userRepository.save(user);

            return AuthResponse.fromEntity(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Transactional
    @Override
    public void logout(LogoutDto logoutDto) {
        validationService.validate(logoutDto);

        User user = userRepository.findById(logoutDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }

    private void generateToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30);
    }
}
