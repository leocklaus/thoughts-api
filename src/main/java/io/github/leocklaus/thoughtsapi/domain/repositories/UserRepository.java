package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.models.UserPicture;
import io.github.leocklaus.thoughtsapi.domain.models.UserPictureTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {
    Optional<User> findByUuid(String uuid);
    Optional<User> findByUsername(String username);

    @Query("from UserPicture u where u.user = :user AND u.pictureType = :type")
    Optional<UserPicture> findPictureByUserAndPictureType(User user, UserPictureTypes type);


    UserDetails findByLogin(String username);

    @Query(value = "SELECT * FROM tb_user u WHERE LOWER(u.username) LIKE %:query% " +
            "OR LOWER(u.first_name) LIKE %:query% OR LOWER(u.last_name) LIKE %:query% " +
            "OR u.email LIKE %:query%",
    nativeQuery = true)
    Page<User> findUserContaining(@Param("query") String query, Pageable pageable);
}
