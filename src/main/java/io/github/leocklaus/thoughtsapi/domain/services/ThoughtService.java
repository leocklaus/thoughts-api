package io.github.leocklaus.thoughtsapi.domain.services;

import io.github.leocklaus.thoughtsapi.api.dto.ThoughtOutputDTO;
import io.github.leocklaus.thoughtsapi.api.dto.ThoughtOutputDTOProjected;
import io.github.leocklaus.thoughtsapi.domain.exceptions.ThoughtNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.models.Likes;
import io.github.leocklaus.thoughtsapi.domain.models.Thought;
import io.github.leocklaus.thoughtsapi.domain.models.ThoughtType;
import io.github.leocklaus.thoughtsapi.domain.projections.ThoughtProjection;
import io.github.leocklaus.thoughtsapi.domain.repositories.LikeRepository;
import io.github.leocklaus.thoughtsapi.domain.repositories.ThoughtRepository;
import io.github.leocklaus.thoughtsapi.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.github.leocklaus.thoughtsapi.domain.models.User;
import io.github.leocklaus.thoughtsapi.api.dto.ThoughtDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ThoughtService {

    @Autowired
    private ThoughtRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserRepository userRepository;


    public ThoughtOutputDTO getThoughtById(Long id) {
        Thought thought = findThoughtByIdOrThrowsNotFoundException(id);
        return new ThoughtOutputDTO(thought);
    }

    public ThoughtOutputDTOProjected getThoughtByUuid(String uuid) {

        Thought thought = findThoughtByUuidOrThrowsNotFoundException(uuid);

        ThoughtProjection thoughtProjected = repository.getThoughtsByUuid(uuid);

        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Optional<Likes> thoughtsLikesByUser = likeRepository.findByUserAndThought(user.getId(), thought.getId());

        ThoughtOutputDTOProjected thoughtDTO = new ThoughtOutputDTOProjected(thoughtProjected);
        thoughtDTO.setLikedByUser(thoughtsLikesByUser.isPresent());

        return thoughtDTO;
    }

    public Page<ThoughtOutputDTOProjected> getAllThoughtsPaged(Pageable pageable){

        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Page<ThoughtProjection> thoughtsProjected = repository.getThoughtsPaged(pageable);
        List<Likes> thoughtsLikesByUser = likeRepository.findByUser(user);

        Page<ThoughtOutputDTOProjected> thoughts = thoughtsProjected.map(thought -> {
            boolean userHasLikedThought = checkIfUserHasLikedTheThought(thought.getUUID(), thoughtsLikesByUser);
                var thoughtDTO =  new ThoughtOutputDTOProjected(thought);
                thoughtDTO.setLikedByUser(userHasLikedThought);
                return thoughtDTO;
        });


        return thoughts;

    }

    private Thought findThoughtByIdOrThrowsNotFoundException(Long id){
        Thought thought = repository.findById(id)
                .orElseThrow(()-> new ThoughtNotFoundException(id));
        return thought;
    }

    private Thought findThoughtByUuidOrThrowsNotFoundException(String uuid){
        Thought thought = repository.findByUuid(uuid)
                .orElseThrow(()-> new ThoughtNotFoundException("Thought não encontrado com o uuid: " + uuid));
        return thought;
    }

    @Transactional()
    public ThoughtOutputDTOProjected saveThought(ThoughtDTO dto) {

        //TODO: GET USER ID FROM TOKEN
        String authenticatedUsername = authorizationService.getAuthenticatedUsername();
        Optional<User> user = userRepository.findByUsername(authenticatedUsername);

        if(user.isEmpty()){
            throw new UserNotFoundException("User not found with username: " + authenticatedUsername);
        }

        Thought thought = new Thought(dto);

        if(dto.getOriginalThoughtUuid() != null){
            Thought originalThought = findThoughtByUuidOrThrowsNotFoundException(dto.getOriginalThoughtUuid());
            thought.setOriginalThought(originalThought);
        }

        thought.setUser(user.get());
        thought = repository.save(thought);
        return new ThoughtOutputDTOProjected(new ThoughtOutputDTO(thought), user.get());
    }

    public Page<ThoughtOutputDTOProjected> getCommentsByThoughtUuid(Pageable pageable, String uuid) {
        Thought thought = findThoughtByUuidOrThrowsNotFoundException(uuid);
        Page<ThoughtProjection> comments = repository.findByOriginalAndType(thought.getId(), ThoughtType.REPLY.toString(), pageable);
        return comments
                .map(ThoughtOutputDTOProjected::new);
    }

    public void addLikeToThought(String uuid) {
        Thought thought = findThoughtByUuidOrThrowsNotFoundException(uuid);

        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Likes likes = new Likes(null, thought, user, LocalDateTime.now());

        if(!likeAlreadyExists(thought, user)){
            likeRepository.save(likes);
        }

    }

    public void deleteLike(String uuid) {
        Thought thought = findThoughtByUuidOrThrowsNotFoundException(uuid);

        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        if(likeAlreadyExists(thought, user)){
            likeRepository.deleteByThoughtAndUser(thought, user);
        }

    }

    @Transactional
    public Page<ThoughtProjection> getThoughtsLikedByUser(Pageable pageable){
        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        //Page<Likes> likes = likeRepository.findAllByUser(user, pageable);

        Page<ThoughtProjection> thoughts = likeRepository.getThoughtsLikesByUser(user.getId(), pageable);

        return thoughts;
    }

    public Page<ThoughtProjection> getUserReplies(Pageable pageable){
        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Page<ThoughtProjection> replies = repository.findByUserAndType(user.getId(), ThoughtType.REPLY.toString(), pageable);

        return replies;
    }

    public Page<ThoughtProjection> getUserThoughts(Pageable pageable){
        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Page<ThoughtProjection> replies = repository.findByUserAndType(user.getId(), ThoughtType.ORIGINAL.toString(), pageable);

        return replies;
    }




    private boolean likeAlreadyExists(Thought thought, User user){
        return getExistingLike(thought, user).isPresent()? true: false;
    }

    private Optional<Likes> getExistingLike(Thought thought, User user){
        return likeRepository.findByThoughtAndUser(thought, user);
    }

    private boolean checkIfUserHasLikedTheThought(String thoughtUUID, List<Likes> thoughtsLiked){
        thoughtsLiked = thoughtsLiked.stream().filter(like -> like.getThought().getUuid() == thoughtUUID).toList();
        return thoughtsLiked.size() == 0 ? false : true;
    }

    public Page<ThoughtOutputDTOProjected> getFollowingUserThougts(Pageable pageable) {
        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Page<ThoughtProjection> thoughtsDTO = repository.getThoughtsFollowedByUser(user.getId(), pageable);

        List<Likes> thoughtsLikesByUser = likeRepository.findByUser(user);

        Page<ThoughtOutputDTOProjected> thoughts = thoughtsDTO.map(thought -> {
            boolean userHasLikedThought = checkIfUserHasLikedTheThought(thought.getUUID(), thoughtsLikesByUser);
            var thoughtDTO =  new ThoughtOutputDTOProjected(thought);
            thoughtDTO.setLikedByUser(userHasLikedThought);
            return thoughtDTO;
        });

        return thoughts;
    }

    public Page<ThoughtOutputDTOProjected> searchThought(String query, Pageable pageable) {

        //TODO: GET USER ID FROM TOKEN
        User user = userService.getUserByIdOrThrowsExceptionIfUserNotExists(1L);

        Page<ThoughtProjection> thoughtProjections = repository.searchThoughtByContent(query.toLowerCase(), pageable);
        List<Likes> thoughtsLikesByUser = likeRepository.findByUser(user);

        Page<ThoughtOutputDTOProjected> thoughts = thoughtProjections.map(thought -> {
            boolean userHasLikedThought = checkIfUserHasLikedTheThought(thought.getUUID(), thoughtsLikesByUser);
            var thoughtDTO = new ThoughtOutputDTOProjected(thought);
            thoughtDTO.setLikedByUser(userHasLikedThought);
            return thoughtDTO;
        });

        return thoughts;
    }
}
