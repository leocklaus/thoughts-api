package io.github.leocklaus.thoughtsapi.infrastructure.service;

import io.github.leocklaus.thoughtsapi.domain.services.PictureStorageService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalPictureStorageService implements PictureStorageService {

    @Value("${thoughtsapi.storage.local.path}")
    private Path filesPath;

    @Override
    public InputStream retrieve(String fileName) {
        var path = getPath(fileName);
        try{
            return Files.newInputStream(path);

        }catch (Exception e){
            throw new StorageException("Não foi possível recuperar o arquivo", e);
        }
    }

    @Override
    public void store(NewPicture newPicture) {

        var path = getPath(newPicture.getFileName());
        
        try{
            FileCopyUtils.copy(newPicture.getInputStream(),
                    Files.newOutputStream(path));
        }catch (Exception e){
            throw new StorageException("Não foi possível armazenar o arquivo", e);
        }



    }

    @Override
    public void delete(String fileName) {
        Path filePath = getPath(fileName);
        try {
            Files.deleteIfExists(filePath);
        }catch (Exception e){
            throw new StorageException("Não foi possível armazenar o arquivo", e);
        }
    }

    private Path getPath(String fileName){
        return filesPath.resolve(Path.of(fileName));
    }



}
