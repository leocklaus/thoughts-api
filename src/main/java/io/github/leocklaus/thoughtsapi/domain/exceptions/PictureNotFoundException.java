package io.github.leocklaus.thoughtsapi.domain.exceptions;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException(){
        super("Picture not found");
    }
}
