package oozaw.theatre.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.AuthResponse;
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
    private ValidationService validationService;

    @Transactional
    @Override
    public AuthResponse register(CreateUserDto createUserDto) {
        validationService.validate(createUserDto);

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(createUserDto.getName());
        user.setPhone(createUserDto.getPhone());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(BCrypt.hashpw(createUserDto.getPassword(), BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiredAt(next30Days());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return AuthResponse.builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public AuthResponse login(LoginDto loginDto) {
        validationService.validate(loginDto);

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return AuthResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30);
    }
}
