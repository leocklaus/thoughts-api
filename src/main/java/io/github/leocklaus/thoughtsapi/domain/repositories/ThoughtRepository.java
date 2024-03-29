package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtOutputDTOProjected;
import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThoughtRepository extends JpaRepository<Thought, Long> {
    Optional<Thought> findByUuid(String uuid);
    Page<Thought> findByOriginalThought(Thought originalThought, Pageable pageable);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content, u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id " +
            "WHERE t.type=:type AND t.user_id=:userId",
            nativeQuery = true)
    Page<ThoughtProjection> findByUserAndType(@Param("userId") Long userId, @Param("type") String type, Pageable pageable);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content, u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id " +
            "WHERE t.type=:type AND t.original_thought=:originalId",
            nativeQuery = true)
    Page<ThoughtProjection> findByOriginalAndType(@Param("originalId") Long originalId, @Param("type") String type, Pageable pageable);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content, u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id",
            nativeQuery = true)
    Page<ThoughtProjection> getThoughtsPaged(Pageable pageable);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content, u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id WHERE t.uuid=:uuid ",
          nativeQuery = true)
    ThoughtProjection getThoughtsByUuid(String uuid);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content,u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id " +
            "WHERE t.type=:type",
            nativeQuery = true)
    Page<ThoughtProjection> getThoughtsFilteredByType(@Param("type") String type, Pageable pageable);

    @Query(value = "SELECT t.uuid, t.type, t.created_at as createdAt, t.content, u.uuid as userUUID, u.username as username, u.first_name as firstName, u.last_name as lastName, COALESCE(r.comments_count, 0) commentsCount, COALESCE(l.likes_count, 0) likesCount\n" +
            "FROM thought t\n" +
            "LEFT JOIN tb_user u ON u.id = t.user_id\n" +
            "LEFT JOIN (\n" +
            "    SELECT original_thought , COUNT(*) comments_count\n" +
            "    FROM thought\n" +
            "    GROUP BY original_thought \n" +
            ") r ON r.original_thought = t.id\n" +
            "LEFT JOIN (\n" +
            "SELECT thought_id, COUNT(*) likes_count\n" +
            "FROM tb_like\n" +
            "GROUP BY thought_id\n" +
            ") l ON l.thought_id = t.id WHERE LOWER(t.content) LIKE %:query%",
            nativeQuery = true)
    Page<ThoughtProjection> searchThoughtByContent(@Param("query") String query, Pageable pageable);

    @Query(value = "select t.id, t.user_id as userId, t.content, t.type, t.uuid, t.created_at as createdAt, t.original_thought as originalThought,  COALESCE(l.likes_count, 0) as likes_count, COALESCE(c.comments_count, 0) as comments_count from follower f left join (thought t left join (\n" +
            "SELECT thought_id, COUNT(*) likes_count FROM tb_like GROUP BY thought_id) l on l.thought_id = t.id) left join (\n" +
            "SELECT original_thought , COUNT(*) comments_count FROM thought GROUP BY original_thought) c on c.original_thought = t.id\n" +
            "on t.user_id = f.followed_id  where f.follower_id=:userId\n" +
            "\n" +
            "\n",
            nativeQuery = true)
    Page<ThoughtProjection> getThoughtsFollowedByUser(@Param("userId") Long userId, Pageable pageable);

    long countByUser(User user);

}
