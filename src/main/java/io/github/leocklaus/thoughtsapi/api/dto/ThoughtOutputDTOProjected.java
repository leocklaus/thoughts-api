package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThoughtOutputDTOProjected {
    private String uuid;
    private String content;
    private String type;
    private String userUUID;
    private String username;
    private String firstname;
    private String lastname;
    private String createdAt;
    private Integer commentsCount;
    private Integer likesCount;
    private Boolean likedByUser;

    public ThoughtOutputDTOProjected(ThoughtProjection projection){
        this.uuid = projection.getUUID();
        this.content = projection.getCONTENT();
        this.type = projection.getTYPE();
        this.userUUID = projection.getUSERUUID();
        this.username = projection.getUSERNAME();
        this.firstname = projection.getFIRSTNAME();
        this.lastname = projection.getLASTNAME();
        this.createdAt = projection.getCREATEDAT();
        this.commentsCount = projection.getCOMMENTSCOUNT();
        this.likesCount = projection.getLIKESCOUNT();
    }

    public ThoughtOutputDTOProjected(ThoughtOutputDTO dto, User user){
        this.uuid = dto.getUuid();
        this.content = dto.getContent();
        this.type = dto.getType().toString();
        this.userUUID = dto.getUserUUID();
        this.username = user.getUsername();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.createdAt = dto.getCreatedAt().toString();
        this.commentsCount = 0;
        this.likesCount = 0;
        this.likedByUser = false;
    }

}
