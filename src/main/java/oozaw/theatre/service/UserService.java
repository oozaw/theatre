package oozaw.theatre.service;

import oozaw.theatre.dto.CreateUserDto;
import oozaw.theatre.dto.UpdateUserDto;
import oozaw.theatre.entity.User;
import oozaw.theatre.model.UserResponse;

import java.util.List;

public interface UserService {

    User create(CreateUserDto createUserDto);

    UserResponse get(String userId);

    List<UserResponse> getAll();

    UserResponse update(UpdateUserDto updateUserDto);

    void delete(String userId);
}
