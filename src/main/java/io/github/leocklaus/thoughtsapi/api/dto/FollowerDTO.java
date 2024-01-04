package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Follower;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDTO {
    private Long id;
    private User followed;
    private User follower;

    public FollowerDTO(Follower follower){
        this.id = follower.getId();
        this.followed = follower.getFollowed();
        this.follower = follower.getFollower();
    }
}
