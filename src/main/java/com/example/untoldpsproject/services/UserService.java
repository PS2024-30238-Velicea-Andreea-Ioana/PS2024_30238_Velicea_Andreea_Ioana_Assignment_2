package com.example.untoldpsproject.services;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.dtos.UserDtoIds;
import com.example.untoldpsproject.entities.User;
import com.example.untoldpsproject.mappers.UserMapper;
import com.example.untoldpsproject.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UUID insert(UserDto userDto){
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db",user.getId());
        return user.getId();
    }

    public List<UserDtoIds> findUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDtoIds findUserById(UUID id){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            LOGGER.error("Person with id {} was not found in db", id);
        }
        return UserMapper.toUserDto(userOptional.get());
    }

    public User updateUserById(UUID id, UserDto updatedUserDto){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            LOGGER.error("Person with id {} was not found in db", id);
        }else{
            User user = userOptional.get();
            User updatedUser = UserMapper.toUser(updatedUserDto);
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            userRepository.save(user);
            LOGGER.debug("User with id {} was successfully updated", id);
        }
        return userOptional.get();
    }

    public void deleteUserById(UUID id){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            LOGGER.error("Person with id {} was not found in db", id);
        }else{
            userRepository.delete(userOptional.get());
        }
    }
}
