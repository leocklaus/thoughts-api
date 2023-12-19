package io.github.leocklaus.thoughtsapi.domain.exceptions;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;

public class ThoughtNotFoundException extends ThoughtException{
    public ThoughtNotFoundException(String msg) {
        super(msg);
    }

    public ThoughtNotFoundException(Long id) {
        super("Não encontramos o thought com o id: " + id);
    }
}
