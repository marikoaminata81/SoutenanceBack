package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.InvalidOperationException;
import com.bezkoder.spring.login.exception.UserNotFoundException;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.payload.response.UserResponse;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.payload.util.FileNamingUtil;
import com.bezkoder.spring.login.payload.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
    /*@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
*/
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    @Autowired
    UserRepository userRepository;
    @Override
    public List<User> lister() {
        return userRepository.findAll();
    }

    @Override
    public User creer(User user) {
        return userRepository.save(user);
    }


    @Override
    public User modifier(User user, Long id) {

        return userRepository.findById(id)
                .map(u ->{
                    u.setNom(user.getNom());
                    u.setPrenom(user.getPrenom());
                    u.setUsername(user.getUsername());
                    u.setNumero(user.getNumero());
                    u.setEmail(user.getEmail());
                    u.setPassword(user.getPassword());
                    u.setAdresse(user.getAdresse());
                    u.setPhoto(user.getPhoto());
                    return userRepository.save(u);
                } ).orElseThrow(() -> new RuntimeException("Cet utilisateur n'existe pas !"));
    }

    @Override
    public String supprimer(Long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès";
    }

    @Override
    public MessageResponse ModifierProfil(User user, Long id) {
        if(userRepository.findById(id) != null){
            User utilisateur = userRepository.findById(id).get();

            utilisateur.setPhoto(user.getPhoto());
            userRepository.save(utilisateur);
            MessageResponse message = new MessageResponse("Photo de profil modifiée avec succès !",true);
            return message;
        }
        else{

            MessageResponse message = new MessageResponse("Photo de profil introuvable !",true);
            return message;

        }
    }

    @Override
    public void resetPassword(User user) {

    }

    @Override
    public void updateUserPassword(User user, String newPassword) {

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserResponse> getFollowerUsersPaginate(Long userId, Integer page, Integer size) {
        User targetUser = getUserById(userId);
        return userRepository.findUsersByFollowingUsers(targetUser,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName")))
                .stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowingUsersPaginate(Long userId, Integer page, Integer size) {
        User targetUser = getUserById(userId);
        return userRepository.findUsersByFollowerUsers(targetUser,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username")))
                .stream().map(this::userToUserResponse).collect(Collectors.toList());
    }


    @Override
    public User updateProfilePhoto(MultipartFile photo) {
        User targetUser = getAuthenticatedUser();
        if (!photo.isEmpty() && photo.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            String oldPhotoName = targetUser.getPhoto();
            String newPhotoName = fileNamingUtil.nameFile(photo);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator + newPhotoName;
            targetUser.setPhoto(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, photo);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, photo);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return userRepository.save(targetUser);
    }



    @Override
    public void followUser(Long userId) {
        User authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            User userToFollow = getUserById(userId);
            authUser.getFollowingUsers().add(userToFollow);
            authUser.setFollowingCount(authUser.getFollowingCount() + 1);
            userToFollow.getFollowerUsers().add(authUser);
            userToFollow.setFollowerCount(userToFollow.getFollowerCount() + 1);
            userRepository.save(userToFollow);
            userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void unfollowUser(Long userId) {
        User authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            User userToUnfollow = getUserById(userId);
            authUser.getFollowingUsers().remove(userToUnfollow);
            authUser.setFollowingCount(authUser.getFollowingCount() - 1);
            userToUnfollow.getFollowerUsers().remove(authUser);
            userToUnfollow.setFollowerCount(userToUnfollow.getFollowerCount() - 1);
            userRepository.save(userToUnfollow);
            userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.err.println("rentre pas");
            return null;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      //  User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        User user1 = userRepository.getReferenceById(userDetails.getId());
        return user1;
    }

    @Override
    public List<UserResponse> getUserSearchResult(String key, Integer page, Integer size) {
        if (key.length() < 3) throw new InvalidOperationException();

        return userRepository.findUsersByUsername(
                key,
                PageRequest.of(page, size)
        ).stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public List<User> getLikesByPostPaginate(Video video, Integer page, Integer size) {
        return userRepository.findUsersByLikedPosts(
                video,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        );
    }


    @Override
    public List<User> getLikesByCommentPaginate(Commentaire commentaire, Integer page, Integer size) {
        return userRepository.findUsersByLikedComments(
                commentaire,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"))
        );
    }


    private UserResponse userToUserResponse(User user) {
        User authUser = getAuthenticatedUser();
        return UserResponse.builder()
                .user(user)
                .followedByAuthUser(user.getFollowerUsers().contains(authUser))
                .build();
    }
    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }
}
