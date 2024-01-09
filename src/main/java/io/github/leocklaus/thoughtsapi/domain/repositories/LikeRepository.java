package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.domain.models.Likes;
import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByThoughtAndUser(Thought thought, User user);

    void deleteByThoughtAndUser(Thought thought, User user);
    Page<Likes> findAllByUser(User user, Pageable pageable);

    @Query(
            value = "SELECT l.created_at as likeDate, t.uuid, t.type, t.content, t.user_id as userId, t.created_at as createdAt, " +
                    "COALESCE(r.comments_count, 0) commentsCount, COALESCE(li.likes_count, 0) likesCount\n" +
                    "FROM tb_like l\n" +
                    "LEFT JOIN thought t ON t.id = l.thought_id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT original_thought , COUNT(*) comments_count\n" +
                    "    FROM thought\n" +
                    "    GROUP BY original_thought\n" +
                    ") r ON r.original_thought = t.id\n" +
                    "LEFT JOIN (\n" +
                    "SELECT thought_id, COUNT(*) likes_count\n" +
                    "FROM tb_like\n" +
                    "GROUP BY thought_id\n" +
                    ") li ON li.thought_id = t.id\n" +
                    "WHERE l.user_id=:userId",
            nativeQuery = true
    )
    Page<ThoughtProjection> getThoughtsLikesByUser(@Param("userId") Long userId, Pageable pageable);


    List<Likes> findByUser(User user);

}
