package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserPasswordDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserWrongPasswordException;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserOutputDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users
                .stream().map(UserOutputDTO::new)
                .toList();
    }

    public UserOutputDTO getUserById(Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        return new UserOutputDTO(user);
    }

    public UserOutputDTO saveUser(UserInputDTO dto) {
        User user = new User(dto);
        setPasswordHashAndUUID(user);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }

    public UserOutputDTO updateUser(UserInputDTO dto, Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        user = fromDTOToUser(dto, user);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }

    public void updateUserPassword(Long id, UserPasswordDTO dto){
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);

        if(user.getPassword().equals(dto.getCurrentPassword())){
            user.setPassword(dto.getNewPassword());
        } else {
            throw new UserWrongPasswordException();
        }

    }

    public void deleteUser(Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        repository.delete(user);
    }

    private User getUserByIdOrThrowsExceptionIfUserNotExists(Long id){
        User user = repository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
        return user;
    }


    private User fromDTOToUser(UserInputDTO dto, User user) {
        user.setId(dto.getId());
        user.setUuid(dto.getUuid());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    private void setPasswordHashAndUUID(User user){
        user.setUuid(generateUUID());
    }

    private String generateUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
