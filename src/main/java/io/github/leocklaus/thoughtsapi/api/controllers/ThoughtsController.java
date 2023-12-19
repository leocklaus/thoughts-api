package io.github.leocklaus.thoughtsapi.api.controllers;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;
import io.github.leocklaus.thoughtsapi.domain.services.ThoughtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/thoughts")
public class ThoughtsController {

    @Autowired
    private ThoughtService thoughtService;

    @GetMapping("/{id}")
    public ResponseEntity<ThoughtDTO> getThoughtById(@PathVariable Long id){
        ThoughtDTO thoughtDTO = thoughtService.getThoughtById(id);
        return ResponseEntity.ok(thoughtDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ThoughtDTO>> getAllThoughtsPaged(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC) Pageable pageable){
        Page<ThoughtDTO> thoughts = thoughtService.getAllThoughtsPaged(pageable);
        return ResponseEntity.ok(thoughts);
    }

    @PostMapping
    public ResponseEntity<ThoughtDTO> saveThought(@RequestBody ThoughtDTO dto){
        ThoughtDTO thought = thoughtService.saveThought(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(thought.getId()).toUri();

        return ResponseEntity.created(uri).body(thought);
    }


}
