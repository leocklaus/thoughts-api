package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.domain.models.Follower;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFollowedAndFollower(User followed, User follower);
    List<Follower> findByFollower(User follower);
    void deleteByFollowedAndFollower(User followed, User follower);
    long countByFollowed(User followed);
    long countByFollower(User follower);
}
