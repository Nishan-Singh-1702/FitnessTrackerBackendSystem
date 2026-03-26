package com.Fitness.service;

import com.Fitness.payload.UserDTO;
import com.Fitness.payload.UserResponse;

public interface UserService {
    UserDTO createNewUser(UserDTO userDTO);
    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);

}
