package com.example.demo.services;

import com.example.demo.entities.Provider;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.io.UserDTO;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = modelMapper.map(userDTO, User.class);
        user.setProvider(user.getProvider() != null ? user.getProvider() : Provider.LOCAL);

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return modelMapper.map(userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User with email " + email + " not found")), UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, String id) {
        UUID userId = UUID.fromString(id);
        User existingUser = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with the given id :" + id));
        if(userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if(userDTO.getImageUrl() != null) existingUser.setImageUrl(userDTO.getImageUrl());
        if(userDTO.getProvider() != null) existingUser.setProvider(userDTO.getProvider());

        if(userDTO.getPassword() != null) existingUser.setPassword(userDTO.getPassword());
        existingUser.setEnabled(userDTO.isEnabled());
        existingUser.setUpdatedAt(Instant.now());
        return modelMapper.map(userRepository.save(existingUser), UserDTO.class);

    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(String id) {
        return modelMapper.map(userRepository.findById(UUID.fromString(id)).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found")),UserDTO.class);
    }

    @Override
    public Iterable<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user ->modelMapper.map(user,UserDTO.class)).toList();
    }
}
