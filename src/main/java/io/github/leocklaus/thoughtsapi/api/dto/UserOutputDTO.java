package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {

    private Long id;
    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private Long follows;
    private Long followers;

    public UserOutputDTO(User user){
        this.id = user.getId();
        this.uuid = user.getUuid();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birthday = user.getBirthday();
    }

}
