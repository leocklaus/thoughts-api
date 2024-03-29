package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.UserInputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.UserPasswordDTO;
import io.github.leocklaus.thoughtsapi.domain.exceptions.NotAuthorizedException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserWrongPasswordException;
import io.github.leocklaus.thoughtsapi.domain.models.Follower;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.domain.repositories.FollowerRepository;
import io.github.leocklaus.thoughtsapi.domain.repositories.ThoughtRepository;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private ThoughtRepository thoughtRepository;

    @Autowired
    private AuthorizationService authorizationService;

    public List<UserOutputDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users
                .stream().map(UserOutputDTO::new)
                .toList();
    }

    public UserOutputDTO getUserById(Long id) {
        User user = getUserByIdOrThrowsExceptionIfUserNotExists(id);
        return new UserOutputDTO(user);
    }

    public Page<UserOutputDTO> searchUsers(String query, Pageable pageable) {

        //logged user
        User loggedUser = getLoggedUserOrThrowsExceptionIfNotExists();

        //followers
        List<Follower> follows = followerRepository.findByFollower(loggedUser);

        Page<UserOutputDTO> users = repository.findUserContaining(query.toLowerCase(), pageable)
                .map(UserOutputDTO::new)
                .map(user -> {
                    user.setLoggedUser(loggedUser.getUsername() == user.getUsername() ? true: false);
                    user.setFollowedByLoggedUser(checkFollowedByLoggedUser(follows, user));
                    return user;
                });


        return users;

    }

    public UserOutputDTO getUserByUuid(String uuid) {
        User user = getUserByUuidOrThrowsExceptionIfUserNotExists(uuid);
        Long follows = followerRepository.countByFollower(user);
        Long followed = followerRepository.countByFollowed(user);
        UserOutputDTO userDTO = new UserOutputDTO(user);
        userDTO.setFollows(follows);
        userDTO.setFollowers(followed);
        return userDTO;
    }

    @Transactional
    public User saveUser(UserInputDTO dto) {
        User user = new User(dto);
        setPasswordHash(user);
        user = repository.save(user);
        return user;
    }

    @Transactional
    public UserOutputDTO updateUser(UserInputDTO dto, Long id) {

        User user = getLoggedUserOrThrowsExceptionIfNotExists();

        if(id != user.getId()){
            throw new NotAuthorizedException();
        }

        user = fromDTOToUser(dto, user);
        user = repository.save(user);
        return new UserOutputDTO(user);
    }

    @Transactional
    public void updateUserPassword(UserPasswordDTO dto){
        User user = getLoggedUserOrThrowsExceptionIfNotExists();

        if(user.getPassword().equals(dto.getCurrentPassword())){
            user.setPassword(dto.getNewPassword());
        } else {
            throw new UserWrongPasswordException();
        }

    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getLoggedUserOrThrowsExceptionIfNotExists();

        if(id != user.getId()){
            throw new NotAuthorizedException();
        }

        repository.delete(user);
    }

    public User getUserByIdOrThrowsExceptionIfUserNotExists(Long id){
        User user = repository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
        return user;
    }

    public User getUserByUuidOrThrowsExceptionIfUserNotExists(String uuid){
        User user = repository.findByUuid(uuid)
                .orElseThrow(()-> new UserNotFoundException("Usuário não econtrado com a id: " + uuid));
        return user;
    }


    private User fromDTOToUser(UserInputDTO dto, User user) {
        user.setId(dto.getId());
        user.setUuid(dto.getUuid());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    private void setPasswordHash(User user){
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    @Transactional
    public void followUser(String userToFollowUuid) {
        User user = getLoggedUserOrThrowsExceptionIfNotExists();
        User userToFollow = getUserByUuidOrThrowsExceptionIfUserNotExists(userToFollowUuid);
        if(!isAlreadyFollowing(user, userToFollow)){
            Follower follower = new Follower(null, userToFollow, user);
            followerRepository.save(follower);
        }
    }

    @Transactional
    public void unfollowUser(String userBeingFollowedUUID) {
        User user = getLoggedUserOrThrowsExceptionIfNotExists();
        User userBeingFollowed = getUserByUuidOrThrowsExceptionIfUserNotExists(userBeingFollowedUUID);
        if(isAlreadyFollowing(user, userBeingFollowed)){
            followerRepository.deleteByFollowedAndFollower(userBeingFollowed, user);
        }
    }

    private Optional<Follower> getFollow(User user, User userToBeFollowed){
        Optional<Follower> follower = followerRepository.findByFollowedAndFollower(userToBeFollowed, user);
        return follower;
    }

    private boolean isAlreadyFollowing(User user, User userToBeFollowed){
        return getFollow(user,userToBeFollowed).isPresent()? true: false;
    }

    private boolean checkFollowedByLoggedUser(List<Follower> loggedUserFollows, UserOutputDTO user){
        loggedUserFollows = loggedUserFollows.stream()
                        .filter(follow -> follow.getFollowed().getUuid() == user.getUuid())
                                .toList();

        return loggedUserFollows.size() == 0 ? false : true;
    }

    public User getLoggedUserOrThrowsExceptionIfNotExists(){
        String authenticatedUsername = authorizationService.getAuthenticatedUsername();
        Optional<User> user = repository.findByUsername(authenticatedUsername);

        if(user.isEmpty()){
            throw new UserNotFoundException("User not found with username: " + authenticatedUsername);
        }

        return user.get();
    }

    public UserOutputDTO getUserByUsername(String username) {
        //get logged user
        User loggedUser = getLoggedUserOrThrowsExceptionIfNotExists();
        Optional<User> user = repository.findByUsername(username);

        if(user.isEmpty()){
            throw new UserNotFoundException("User not found with username: " + username);
        }

        Long follows = followerRepository.countByFollower(user.get());
        Long followed = followerRepository.countByFollowed(user.get());
        Long thoughtCount = thoughtRepository.countByUser(user.get());

        UserOutputDTO userDTO = new UserOutputDTO(user.get());
        userDTO.setFollowers(followed);
        userDTO.setFollows(follows);
        userDTO.setLoggedUser(loggedUser.getUsername() == user.get().getUsername() ? true: false);
        userDTO.setPostsCount(thoughtCount);

        return userDTO;
    }
}
