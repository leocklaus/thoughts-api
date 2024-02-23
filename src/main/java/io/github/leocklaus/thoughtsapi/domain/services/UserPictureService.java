package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.PictureUploadDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.NotAuthorizedException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.PictureNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.models.UserPicture;
import io.github.leocklaus.thoughtsapi.domain.models.UserPictureTypes;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserPictureService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PictureStorageService pictureStorageService;


    @Transactional
    public UserPicture saveUserPicture(PictureUploadDTO dto, String username, UserPictureTypes type) throws IOException {

        var user = userService.getLoggedUserOrThrowsExceptionIfNotExists();

        String existingFileName = null;

        if(isNotTheLoggedUser(user, username)){
            throw new NotAuthorizedException();
        }


        Optional<UserPicture> existingPicture = userRepository.findPictureByUserAndPictureType(user, type);

        if(existingPicture.isPresent()){
            existingFileName = existingPicture.get().getFileName();
            userRepository.deleteUserPicture(existingPicture.get());
            pictureStorageService.delete(existingFileName);
        }

        var file = dto.file();

        var newPictureName = pictureStorageService.generateNewFileName(file.getOriginalFilename());

        var userPicture = new UserPicture();

        userPicture.setUser(user);
        userPicture.setPictureType(type);
        userPicture.setContentType(file.getContentType());
        userPicture.setContentSize(file.getSize());
        userPicture.setFileName(newPictureName);

        userPicture =  userRepository.save(userPicture);
        userRepository.flush();

        var newPicture = PictureStorageService.NewPicture.builder()
                .fileName(newPictureName)
                .inputStream(file.getInputStream())
                .build();

        pictureStorageService.store(newPicture);

        return userPicture;
    }

    public UserPicture getUserPicture(String username, UserPictureTypes type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        var userPicture = userRepository.findPictureByUserAndPictureType(user, type);

        if(userPicture.isEmpty()){
            throw new PictureNotFoundException();
        }

        return userPicture.get();
    }

    private boolean isNotTheLoggedUser(User user, String username){
        return user.getUsername().equals(username)? false : true;
    }

    @Transactional
    public void deleteUserPicture(String username, UserPictureTypes types) {

        var userPicture = this.getUserPicture(username, types);

        pictureStorageService.delete(userPicture.getFileName());
        userRepository.deleteUserPicture(userPicture);
    }
}
