package io.github.leocklaus.thoughtsapi.api.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {
    private String currentPassword;
    private String newPassword;
}
