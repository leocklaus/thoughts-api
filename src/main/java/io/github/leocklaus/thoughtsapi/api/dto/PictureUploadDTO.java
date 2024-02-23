package io.github.leocklaus.thoughtsapi.api.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PictureUploadDTO(@NotNull MultipartFile file) {
}
