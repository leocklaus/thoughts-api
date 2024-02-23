package io.github.leocklaus.thoughtsapi.domain.repositories;

import io.github.leocklaus.thoughtsapi.domain.models.UserPicture;

public interface UserRepositoryQueries {

    UserPicture save(UserPicture picture);

    void deleteUserPicture(UserPicture userPicture);

}
