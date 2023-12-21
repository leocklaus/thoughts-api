package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Thought {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String uuid;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(nullable = false)
    private ThoughtType type;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "originalThought")
    private Thought originalThought;
    @OneToMany(mappedBy = "thought")
    private List<Like> likes = new ArrayList<>();

    public Thought(ThoughtDTO dto){
        this.id = dto.getId();
        this.uuid = dto.getUuid();
        this.content = dto.getContent();
        this.type = dto.getType();
        this.createdAt = dto.getCreatedAt();
    }

    @PrePersist
    public void generateUUID(){
        setUuid(UUID.randomUUID().toString());
    }

}
