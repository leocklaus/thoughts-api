package io.github.leocklaus.thoughtsapi.domain.exceptions;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(){
        super("Requisição não autorizada ou com credenciais inválidas");
    }
}
