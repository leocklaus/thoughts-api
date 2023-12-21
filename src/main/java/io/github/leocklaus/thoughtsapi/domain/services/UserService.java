package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserPasswordDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserWrongPasswordException;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional
    public UserOutputDTO saveUser(UserInputDTO dto) {
        User user = new User(dto);
        setPasswordHash(user);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }

    @Transactional
    public UserOutputDTO updateUser(UserInputDTO dto, Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        user = fromDTOToUser(dto, user);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }

    @Transactional
    public void updateUserPassword(Long id, UserPasswordDTO dto){
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);

        if(user.getPassword().equals(dto.getCurrentPassword())){
            user.setPassword(dto.getNewPassword());
        } else {
            throw new UserWrongPasswordException();
        }

    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        repository.delete(user);
    }

    public User getUserByIdOrThrowsExceptionIfUserNotExists(Long id){
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

    private void setPasswordHash(User user){    }

}
