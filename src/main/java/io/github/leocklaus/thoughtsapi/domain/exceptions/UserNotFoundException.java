package io.github.leocklaus.thoughtsapi.domain.exceptions;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(Long id) {
        super("Não encontramos o usuário com o id: " + id);
    }
}
