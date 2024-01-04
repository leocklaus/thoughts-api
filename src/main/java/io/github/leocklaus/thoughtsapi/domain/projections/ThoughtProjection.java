package io.github.leocklaus.thoughtsapi.domain.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;

import java.time.LocalDateTime;

public interface ThoughtProjection {
    String getUUID();
    String getCONTENT();
    String getTYPE();
    @JsonProperty("userId")
    Long getUSERID();
    @JsonProperty("createdAt")
    LocalDateTime getCREATEDAT();
    @JsonProperty("commentsCount")
    Integer getCOMMENTSCOUNT();
    @JsonProperty("likesCount")
    Integer getLIKESCOUNT();
}
