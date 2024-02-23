package io.github.leocklaus.thoughtsapi.infrastructure.repository;

import io.github.leocklaus.thoughtsapi.domain.models.UserPicture;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public UserPicture save(UserPicture picture) {
        return entityManager.merge(picture);
    }
    @Transactional
    @Override
    public void deleteUserPicture(UserPicture userPicture) {
        entityManager.remove(userPicture);
    }


}
