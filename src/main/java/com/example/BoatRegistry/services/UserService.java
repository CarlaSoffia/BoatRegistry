package com.example.BoatRegistry.services;

import com.example.BoatRegistry.dtos.users.UserCreateRequestDto;
import com.example.BoatRegistry.dtos.users.UserResponseDto;
import com.example.BoatRegistry.dtos.users.UserUpdateRequestDto;
import com.example.BoatRegistry.entities.BoatType;
import com.example.BoatRegistry.entities.User;
import com.example.BoatRegistry.mappers.UserMapper;
import com.example.BoatRegistry.repositories.BoatImageRepository;
import com.example.BoatRegistry.repositories.BoatRepository;
import com.example.BoatRegistry.repositories.BoatTypeRepository;
import com.example.BoatRegistry.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BoatRepository boatRepository;
    private final BoatTypeRepository boatTypeRepository;
    private final BoatImageRepository boatImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final String NotFoundMessage = "User with %s %s not found";

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper, BoatRepository boatRepository, BoatTypeRepository boatTypeRepository, BoatImageRepository boatImageRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.boatRepository = boatRepository;
        this.boatTypeRepository = boatTypeRepository;
        this.boatImageRepository = boatImageRepository;
    }

    public UserResponseDto save(UserCreateRequestDto userRequestDto) {
        validateIfUserExistsWithEmail(userRequestDto.getEmail());
        var userToInsert = userMapper.toEntity(userRequestDto);
        userToInsert.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        var insertedUser =  userRepository.save(userToInsert);
        return userMapper.toResponseDto(insertedUser);
    }

    public UserResponseDto getById(String email) {
        var user = getUserByEmail(email);
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto update(UserUpdateRequestDto userRequestDto, String email) {
        var user = getUserByEmail(email);

        var newName = userRequestDto.getName();
        var newEmail = userRequestDto.getEmail();
        var newPassword = userRequestDto.getPassword();

        var updateName = newName != null && !newName.equals(user.getName());
        var updateEmail = newEmail != null && !newEmail.equals(user.getEmail());
        var updatePassword = newPassword != null && !newPassword.equals(user.getPassword());

        if (updateName) {
            user.setName(newName);
        }

        if (updateEmail) {
            validateIfUserExistsWithEmail(newEmail);
            user.setEmail(newEmail);
        }

        if (updatePassword) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        if(updateName || updateEmail || updatePassword) {
            userRepository.save(user);
        }
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto delete(String email) {
        var user = getUserByEmail(email);
        var boats = boatRepository.findByUserEmail(email);
        for(var boat : boats) {
            var boatImage = boat.getBoatImage();
            if(boatImage != null) {
                boatImageRepository.delete(boatImage);
            }
        }
        boatRepository.deleteAll(boats);

        var boatTypes = boatTypeRepository.findByUserEmail(email);
        boatTypeRepository.deleteAll(boatTypes);

        userRepository.delete(user);
        return userMapper.toResponseDto(user);
    }


    public void anyWithEmail(String email) {
        var userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage,"email", email));
        }
    }

    private User getUserByEmail(String userEmail) {
        var userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage,"email", userEmail));
        }

        return userOptional.get();
    }

    private void validateIfUserExistsWithEmail(String userEmail) {
        var userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isPresent()) {
            throw new DataIntegrityViolationException(String.format("A user with email %s already exists", userEmail));
        }

    }
}
