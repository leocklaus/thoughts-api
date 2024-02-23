package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String uuid;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String login;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String birthday;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String bio;
    private String profilePicture;
    private String coverPicture;
    private UserRoles roles;

    public User(UserInputDTO dto){
        this.id = dto.getId();
        this.uuid = dto.getUuid();
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.birthday = dto.getBirthday();
        this.password = dto.getPassword();
        this.bio = dto.getBio();
    }

    @PrePersist
    public void generateCode(){

        setUuid(UUID.randomUUID().toString());
        setRoles(UserRoles.USER);
        setLogin(this.getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.roles == UserRoles.ADMIN){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
