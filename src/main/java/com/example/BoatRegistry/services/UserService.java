package com.example.BoatRegistry.services;

import com.example.BoatRegistry.dtos.users.UserRequestDto;
import com.example.BoatRegistry.dtos.users.UserResponseDto;
import com.example.BoatRegistry.entities.BoatType;
import com.example.BoatRegistry.entities.User;
import com.example.BoatRegistry.mappers.UserMapper;
import com.example.BoatRegistry.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String NotFoundMessage = "User with %s %s not found";

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto save(UserRequestDto userRequestDto) {
        var userToInsert = userMapper.toEntity(userRequestDto);
        userToInsert.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        var insertedUser =  userRepository.save(userToInsert);
        return userMapper.toResponseDto(insertedUser);
    }

    public UserResponseDto getById(Long id, String email) {
        var user = validateAccessToUser(id, email);
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto update(Long id, UserRequestDto userRequestDto, String email) {
        var user = validateAccessToUser(id, email);

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

    public UserResponseDto delete(Long id, String email) {
        var user = validateAccessToUser(id, email);
        userRepository.delete(user);
        return userMapper.toResponseDto(user);
    }


    public void anyWithEmail(String email) {
        var userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage,"email", email));
        }
    }

    private User validateAccessToUser(Long id, String userEmail) {
        var userOptional = userRepository.findById(id);

        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(NotFoundMessage, "id", id));
        }
        var user = userOptional.get();

        if(!user.getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have permission to access this user");
        }
        return user;
    }
}
