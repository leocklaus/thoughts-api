package io.github.leocklaus.thoughtsapi.api.controllers;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;
import io.github.leocklaus.thoughtsapi.api.dto.ThoughtOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.ThoughtOutputDTOProjected;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import io.github.leocklaus.thoughtsapi.domain.services.ThoughtService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    @GetMapping("/search")
    public ResponseEntity<Page<ThoughtOutputDTOProjected>> searchThoughts(@RequestParam String query, Pageable pageable){
        Page<ThoughtOutputDTOProjected> thoughtDTO = thoughtService.searchThought(query, pageable);
        return ResponseEntity.ok(thoughtDTO);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ThoughtOutputDTOProjected> getThoughtByUuid(@PathVariable String uuid){
        ThoughtOutputDTOProjected thoughtDTO = thoughtService.getThoughtByUuid(uuid);
        return ResponseEntity.ok(thoughtDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ThoughtOutputDTOProjected>> getAllThoughtsPaged(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = {"created_at"}) Pageable pageable){
        Page<ThoughtOutputDTOProjected> thoughts = thoughtService.getAllThoughtsPaged(pageable);
        return ResponseEntity.ok(thoughts);
    }

    @PostMapping
    public ResponseEntity<ThoughtOutputDTOProjected> saveThought(@RequestBody @Valid ThoughtDTO dto){
        ThoughtOutputDTOProjected thought = thoughtService.saveThought(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(thought.getUuid()).toUri();

        return ResponseEntity.created(uri).body(thought);
    }

    @GetMapping("/{uuid}/comments")
    public ResponseEntity<Page<ThoughtOutputDTOProjected>> getThoughComments(
            @PathVariable String uuid,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = {"createdAt"}) Pageable pageable){
        Page<ThoughtOutputDTOProjected> comments = thoughtService.getCommentsByThoughtUuid(pageable, uuid);
        return ResponseEntity.ok(comments);
    }

    @Transactional
    @PutMapping("/{uuid}/like")
    public ResponseEntity<?> addLikeToThought(@PathVariable String uuid){
        thoughtService.addLikeToThought(uuid);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{uuid}/like")
    public ResponseEntity<?> deleteLike(@PathVariable String uuid){
        thoughtService.deleteLike(uuid);
        return ResponseEntity.ok().build();
    }


}
