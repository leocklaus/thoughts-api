package io.github.leocklaus.thoughtsapi.domain.exceptions;

public class UserWrongPasswordException extends UserException{
    public UserWrongPasswordException(){
        super("A senha enviada está incorreta");
    }
}
