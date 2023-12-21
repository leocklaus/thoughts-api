package io.github.leocklaus.thoughtsapi.domain.models;

import io.github.leocklaus.thoughtsapi.api.dto.FollowerDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long followerId;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User followed;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User follower;

    public Follower(FollowerDTO dto){
        this.id = dto.getId();
        this.userId = dto.getUserId();
        this.followerId = dto.getFollowerId();
    }
}
