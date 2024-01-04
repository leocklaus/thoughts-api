package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThoughtOutputDTOProjected {
    private String UUID;
    private String CONTENT;
    private ThoughtType TYPE;
    private Long USERID;
    private LocalDateTime CREATEDAT;
    private Integer COMMENTSCOUNT;
    private Integer LIKESCOUNT;

}
