package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThoughtRepository extends JpaRepository<Thought, Long> {
}
