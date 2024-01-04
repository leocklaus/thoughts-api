package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThoughtDTO {
    private Long id;
    private String uuid;
    @NotBlank @Size(max = 280)
    private String content;
    @NotNull
    private ThoughtType type;
    private User user;
    private String originalThoughtUuid;
    private LocalDateTime createdAt;

}

