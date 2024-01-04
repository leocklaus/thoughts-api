package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThoughtOutputDTO {
    private String uuid;
    private String content;
    private ThoughtType type;
    private String userUUID;
    private LocalDateTime createdAt;

    public ThoughtOutputDTO(Thought thought){
        this.uuid = thought.getUuid();
        this.content = thought.getContent();
        this.type = thought.getType();
        this.userUUID = thought.getUser().getUuid();
        this.createdAt = thought.getCreatedAt();
    }
}
