package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String birthday;
    @Column(nullable = false)
    private String password;

    public User(UserInputDTO dto){
        this.id = dto.getId();
        this.uuid = dto.getUuid();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.birthday = dto.getBirthday();
        this.password = dto.getPassword();
    }
}
