package io.github.leocklaus.thoughtsapi.api.controllers;

import io.github.leocklaus.thoughtsapi.api.dto.*;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import io.github.leocklaus.thoughtsapi.domain.services.ThoughtService;
import io.github.leocklaus.thoughtsapi.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThoughtService thoughtService;

    @GetMapping
    public ResponseEntity<List<UserOutputDTO>> getAll(){
        List<UserOutputDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserOutputDTO> getUserById(@PathVariable String uuid){
        UserOutputDTO user = userService.getUserByUuid(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserOutputDTO> saveUser(@RequestBody UserInputDTO dto){
        UserOutputDTO user = userService.saveUser(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping("/{id}/updatepassword")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDTO dto){
        userService.updateUserPassword(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDTO> updateUser(@PathVariable Long id, @RequestBody UserInputDTO dto){
        UserOutputDTO user = userService.updateUser(dto, id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current/details")
    public ResponseEntity<UserOutputDTO> getCurrentUserDetails(){
        //TODO: GET USER ID VIA JWT
        Long currentUserId = 1L;
        UserOutputDTO user = userService.getUserById(currentUserId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current/likes")
    public ResponseEntity<Page<ThoughtProjection>> getThoughtsLikedByUSer(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = {"likeDate"}) Pageable pageable){
        Page<ThoughtProjection> thoughtsProjection = thoughtService.getThoughtsLikedByUser(pageable);
        return ResponseEntity.ok(thoughtsProjection);
    }

    @GetMapping("/current/replies")
    public ResponseEntity<Page<ThoughtProjection>> getUserReplies(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = {"createdAt"}) Pageable pageable){
        Page<ThoughtProjection> replies = thoughtService.getUserReplies(pageable);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/current/thoughts")
    public ResponseEntity<Page<ThoughtProjection>> getCurrentUserThoughts(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = {"createdAt"}) Pageable pageable){

        Page<ThoughtProjection> replies = thoughtService.getUserThoughts(pageable);
        return ResponseEntity.ok(replies);
    }

    @PutMapping("/follow/{userToFollowUuid}")
    public ResponseEntity<?> addFollower(@PathVariable String userToFollowUuid){
        userService.followUser(userToFollowUuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/follow/{userToUnfollowUuid}")
    public ResponseEntity<?> removeFollower(@PathVariable String userToUnfollowUuid){
        userService.unfollowUser(userToUnfollowUuid);
        return ResponseEntity.noContent().build();
    }



}
