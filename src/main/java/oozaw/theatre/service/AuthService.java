package oozaw.theatre.service;

import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.LoginDto;
import oozaw.theatre.dto.LogoutDto;
import oozaw.theatre.model.AuthResponse;

public interface AuthService {
    AuthResponse register(CreateUserDto createUserDto);

    AuthResponse login(LoginDto loginDto);

    void logout(LogoutDto logoutDto);
}
