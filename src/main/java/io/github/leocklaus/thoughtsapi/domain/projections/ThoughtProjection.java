package io.github.leocklaus.thoughtsapi.domain.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import lombok.Getter;

import java.time.LocalDateTime;


public interface ThoughtProjection {
    String getUUID();
    String getCONTENT();
    String getTYPE();
    @JsonProperty("userUUID")
    String getUSERUUID();
    @JsonProperty("createdAt")
    String getCREATEDAT();
    @JsonProperty("commentsCount")
    Integer getCOMMENTSCOUNT();
    @JsonProperty("likesCount")
    Integer getLIKESCOUNT();
    String getUSERNAME();
    String getFIRSTNAME();
    String getLASTNAME();
    Boolean likesByUser();
}
