package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.ThoughtNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.repositories.ThoughtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThoughtService {

    @Autowired
    private ThoughtRepository repository;


    public ThoughtDTO getThoughtById(Long id) {
        Thought thought = findThoughtByIdOrThrowsNotFoundException(id);
        return new ThoughtDTO(thought);
    }

    public Page<ThoughtDTO> getAllThoughtsPaged(Pageable pageable){
        Page<Thought> thoughts = repository.findAll(pageable);
        return thoughts
                .map(ThoughtDTO::new);
    }

    private Thought findThoughtByIdOrThrowsNotFoundException(Long id){
        Thought thought = repository.findById(id)
                .orElseThrow(()-> new ThoughtNotFoundException(id));
        return thought;
    }

    public ThoughtDTO saveThought(ThoughtDTO dto) {
        //TODO: GET USER ID FROM TOKEN
        dto.setId(1L);
        Thought thought = new Thought(dto);
        thought = repository.save(thought);
        return new ThoughtDTO(thought);
    }
}
