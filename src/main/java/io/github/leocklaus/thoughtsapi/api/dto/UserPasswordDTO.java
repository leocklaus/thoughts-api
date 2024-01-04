package io.github.leocklaus.thoughtsapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordDTO {
    @NotBlank @Size(min = 6)
    private String currentPassword;
    @NotBlank @Size(min = 6)
    private String newPassword;
}
