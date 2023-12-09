package io.github.leocklaus.thoughtsapi.api.controllers;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserPasswordDTO;
import io.github.leocklaus.thoughtsapi.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<UserOutputDTO>> getAll(){
        List<UserOutputDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDTO> getUserById(@PathVariable Long id){
        UserOutputDTO user = userService.getUserById(id);
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



}
