package io.github.leocklaus.thoughtsapi.api.controllers;

import io.github.leocklaus.thoughtsapi.api.dto.PictureUploadDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserPictureOutputDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.PictureNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.models.UserPicture;
import io.github.leocklaus.thoughtsapi.domain.models.UserPictureTypes;
import io.github.leocklaus.thoughtsapi.domain.services.PictureStorageService;
import io.github.leocklaus.thoughtsapi.domain.services.UserPictureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/users/pictures/{username}")
public class UserPictureController {

    @Autowired
    private UserPictureService userPictureService;

    @Autowired
    private PictureStorageService pictureStorageService;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPictureOutputDTO> getProfilePicture(@PathVariable String username){
        var userPicture = userPictureService.getUserPicture(username, UserPictureTypes.PROFILE);

        var userPictureDTO = fromUserPictureToDTO(userPicture);
        return ResponseEntity.ok(userPictureDTO);
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<InputStreamResource> getProfilePictureStream(@PathVariable String username){

       try {
           var userPicture = userPictureService.getUserPicture(username, UserPictureTypes.PROFILE);
           var inputStream = pictureStorageService.retrieve(userPicture.getFileName());

           return ResponseEntity
                   .ok()
                   .contentType(MediaType.IMAGE_JPEG)
                   .body(new InputStreamResource(inputStream));

       }catch (PictureNotFoundException e){
            ResponseEntity.notFound().build();
       }catch (UserNotFoundException e){
           ResponseEntity.notFound().build();
       }


        return null;
    }

    @GetMapping(value = "/cover", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPictureOutputDTO> getCoverPicture(@PathVariable String username){
        var userPicture = userPictureService.getUserPicture(username, UserPictureTypes.COVER);

        var userPictureDTO = fromUserPictureToDTO(userPicture);
        return ResponseEntity.ok(userPictureDTO);
    }

    @GetMapping(value = "/cover")
    public ResponseEntity<InputStreamResource> getCoverPictureStream(@PathVariable String username){

        try {
            var userPicture = userPictureService.getUserPicture(username, UserPictureTypes.COVER);
            var inputStream = pictureStorageService.retrieve(userPicture.getFileName());

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(inputStream));

        }catch (PictureNotFoundException e){
            ResponseEntity.notFound().build();
        }catch (UserNotFoundException e){
            ResponseEntity.notFound().build();
        }


        return null;
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserPictureOutputDTO> addProfilePicture(@PathVariable String username, @Valid PictureUploadDTO dto) throws IOException {

        var userPicture = userPictureService.saveUserPicture(dto, username, UserPictureTypes.PROFILE);

        var userPictureDTO = fromUserPictureToDTO(userPicture);

        return ResponseEntity.ok(userPictureDTO);

    }

    @PutMapping(value = "/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserPictureOutputDTO> addCoverPicture(@PathVariable String username, @Valid PictureUploadDTO dto) throws IOException {

        var userPicture = userPictureService.saveUserPicture(dto, username, UserPictureTypes.COVER);

        var userPictureDTO = fromUserPictureToDTO(userPicture);

        return ResponseEntity.ok(userPictureDTO);

    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable String username){
        userPictureService.deleteUserPicture(username, UserPictureTypes.PROFILE);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cover")
    public ResponseEntity<?> deleteCoverPicture(@PathVariable String username){
        userPictureService.deleteUserPicture(username, UserPictureTypes.COVER);
        return ResponseEntity.noContent().build();
    }

    private UserPictureOutputDTO fromUserPictureToDTO(UserPicture userPicture){
        return new UserPictureOutputDTO(
                userPicture.getPictureType(),
                userPicture.getContentType(),
                userPicture.getContentSize(),
                userPicture.getFileName()
        );
    }


}
