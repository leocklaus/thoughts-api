package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {

    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private Long follows;
    private Long followers;
    private Long postsCount;
    private boolean isLoggedUser;
    private boolean followedByLoggedUser;
    private String bio;

    public UserOutputDTO(User user){
        this.uuid = user.getUuid();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthday = user.getBirthday();
        this.bio = user.getBio();
    }

}
