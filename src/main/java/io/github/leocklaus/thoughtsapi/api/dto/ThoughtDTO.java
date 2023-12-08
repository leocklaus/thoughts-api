package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThoughtDTO {
    private Long id;
    private String uuid;
    private Long userId;
    private String content;
    private ThoughtType type;
    private Long originalThoughtId;
    private LocalDateTime createdAt;

    public ThoughtDTO(Thought thought){
        this.id = thought.getId();
        this.uuid = thought.getUuid();
        this.userId = thought.getUserId();
        this.content = thought.getContent();
        this.type = thought.getType();
        this.originalThoughtId = thought.getOriginalThoughtId();
        this.createdAt = thought.getCreatedAt();
    }
}

