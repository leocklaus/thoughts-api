package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.LikesDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_like")
public class Likes {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Thought thought;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Likes(LikesDTO dto){
        this.id = dto.getId();
        this.thought = dto.getThought();
        this.user = dto.getUser();
    }
}
