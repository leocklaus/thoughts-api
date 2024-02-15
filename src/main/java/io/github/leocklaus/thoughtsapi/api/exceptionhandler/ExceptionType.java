package io.github.leocklaus.thoughtsapi.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionType {

    USER_NOT_FOUND("Usuário não encontrado", "/user-not-found"),
    THOUGHT_NOT_FOUND("Thought não encontrado", "/thought-not-found"),
    INVALID_DATA("Dados inválidos", "/invalid-data"),
    RESOURCE_NOT_FOUND("Recurso não encontrado", "/resource-not-found"),
    WRONG_PASSWORD("A senha digitada é incorreta", "/wrong-password"),
    NOT_AUTHORIZED("Requisição não autorizada ou credenciais inválidas", "/not-authorized");

    private String title;
    private String URI;

    ExceptionType(String title, String URI){
        this.title = title;
        this.URI = URI;
    }
}
