package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    List<User> lister();

    User creer(User user);

    User modifier(User user, Long id);

    String supprimer(Long id);
    MessageResponse ModifierProfil(User user, Long id);

    public void resetPassword(User user);

    public void updateUserPassword(User user, String newPassword);

    User findByEmail(String email);


    List<User> getFollowerUsersPaginate(Long userId);
    List<User> getFollowingUsersPaginate(Long userId);

    User updateProfilePhoto(MultipartFile photo);
    User getUserById(Long userId);

    void followUser(Long userId);
    void unfollowUser(Long userId);
    User getAuthenticatedUser();

    List<User> getUserSearchResult(String key);
    List<User> getLikesByPostPaginate(Video video);
    List<User> getLikesByCommentPaginate(Commentaire commentaire);


}
