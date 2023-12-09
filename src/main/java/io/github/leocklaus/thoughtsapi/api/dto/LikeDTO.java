package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Like;
import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private Long thoughtId;
    private Long userId;

    public LikeDTO(Like like){
        this.id = like.getId();
        this.thoughtId = like.getThoughtId();
        this.userId = like.getUserId();
    }
}
