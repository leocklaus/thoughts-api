package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Likes;
import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesDTO {
    private Long id;
    private Thought thought;
    private User user;

    public LikesDTO(Likes likes){
        this.id = likes.getId();
        this.thought = likes.getThought();
        this.user = likes.getUser();
    }
}
