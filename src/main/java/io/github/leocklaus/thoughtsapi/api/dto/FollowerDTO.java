package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Follower;
import lombok.Data;

@Data
public class FollowerDTO {
    private Long id;
    private Long userId;
    private Long followerId;

    public FollowerDTO(Follower follower){
        this.id = follower.getId();
        this.userId = follower.getUserId();
        this.followerId = follower.getFollowerId();
    }
}
