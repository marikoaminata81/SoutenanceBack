package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    Video getPostById(Long postId);
    PostResponse getPostResponseById(Long postId);
    List<Video> getPostsByUserPaginate(User author);
   // List<PostResponse> getTimelinePostsPaginate(Integer page, Integer size);
    List<Video> getPostSharesPaginate(Video sharedPost);
   // List<PostResponse> getPostByTagPaginate(Tag tag, Integer page, Integer size);
    Video createNewPost(String titre, MultipartFile imagecouverture,MultipartFile url);
    Video updatePost(Long postId, String titre, MultipartFile imagecouverture,MultipartFile url);
    void deletePost(Long postId);
    void likePost(Long postId);
    void unlikePost(Long postId);
    Commentaire createPostComment(Long postId, String contenue);
    Commentaire updatePostComment(Long commentId, Long postId, String contenue);
    void deletePostComment(Long commentId, Long postId);
    Video createPostShare(String content, Long postShareId);
    Video updatePostShare(String content, Long postShareId);
    void deletePostShare(Long postShareId);
    boolean verifyLikeByUser(Long postId, Long userId);
}
