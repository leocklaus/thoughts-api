package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThoughtDTO {
    private Long id;
    private String uuid;
    private String content;
    private ThoughtType type;
    private User user;
    private Thought originalThought;
    private LocalDateTime createdAt;

    public ThoughtDTO(Thought thought){
        this.id = thought.getId();
        this.uuid = thought.getUuid();
        this.content = thought.getContent();
        this.type = thought.getType();
        this.originalThought = thought.getOriginalThought();
        this.user = thought.getUser();
        this.createdAt = thought.getCreatedAt();
    }
}

