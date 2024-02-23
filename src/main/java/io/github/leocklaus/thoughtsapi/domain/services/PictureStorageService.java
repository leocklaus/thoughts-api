package io.github.leocklaus.thoughtsapi.domain.services;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.UUID;

public interface PictureStorageService {

    InputStream retrieve(String fileName);

    void store(NewPicture newPicture);
    void delete(String fileName);

    @Getter
    @Builder
    class NewPicture{
        private String fileName;
        private InputStream inputStream;
    }

    default String generateNewFileName(String fileName){
        return UUID.randomUUID() + "_" + fileName;
    }

}
