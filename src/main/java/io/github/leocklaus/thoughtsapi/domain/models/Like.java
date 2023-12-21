package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.LikeDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Like {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long thoughtId;
    @Column(nullable = false)
    private Long userId;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Thought thought;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Like(LikeDTO dto){
        this.id = dto.getId();
        this.thoughtId = dto.getThoughtId();
        this.userId = dto.getUserId();
    }
}
