package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserOutputDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
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

    private User getUserByIdOrThrowsExceptionIfUserNotExists(Long id){
        User user = repository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
        return user;
    }

    public UserOutputDTO saveUser(UserInputDTO dto) {
        User user = fromDTOToUser(dto);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }


    private User fromDTOToUser(UserInputDTO dto) {
        User user = new User(dto);

        if(user.getUuid() == null){
            user.setUuid(generateUUID());
        }

        return user;
    }

    private String generateUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
