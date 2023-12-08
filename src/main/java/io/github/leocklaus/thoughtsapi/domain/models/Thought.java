package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Thought {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String uuid;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private ThoughtType type;
    private Long originalThoughtId;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Thought(ThoughtDTO dto){
        this.id = dto.getId();
        this.uuid = dto.getUuid();
        this.userId = dto.getUserId();
        this.content = dto.getContent();
        this.type = dto.getType();
        this.originalThoughtId = dto.getOriginalThoughtId();
        this.createdAt = dto.getCreatedAt();
    }

}
