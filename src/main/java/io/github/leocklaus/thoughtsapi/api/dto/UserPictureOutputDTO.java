package io.github.leocklaus.thoughtsapi.api.dto;

import io.github.leocklaus.thoughtsapi.domain.models.UserPictureTypes;

public record UserPictureOutputDTO(UserPictureTypes pictureType, String contentType, Long contentSize, String fileName) {
}
