package com.example.demo.services;

import com.example.demo.io.UserDTO;
import org.springframework.stereotype.Service;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email);

    UserDTO updateUser(UserDTO userDTO,String id);

    void deleteUser(String id);

    UserDTO getUserById(String id);

    Iterable<UserDTO> getAllUsers();
}
