package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.exception.EmptyPostException;
import com.bezkoder.spring.login.image.ConfigImage;
import com.bezkoder.spring.login.image.saveImg;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.CommentResponse;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.payload.response.PostResponse;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.repository.VideoRepository;
import com.bezkoder.spring.login.security.services.CommentaireService;
import com.bezkoder.spring.login.security.services.UserService;
import com.bezkoder.spring.login.security.services.VideoService;
import com.bezkoder.spring.login.security.services.VideoServiceImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final VideoServiceImpl videoServiceImpl;
    private final CommentaireService commentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @PostMapping("/posts/create")

    public ResponseEntity<Video> uploadVideoAndPhoto(

            @RequestParam("titre") String titre,
            @RequestParam("imagecouverture") MultipartFile imagecouverture,
            @RequestParam("url") MultipartFile url

    ) throws IOException {



        Video entity = videoServiceImpl.saveVideoAndPhoto(titre,imagecouverture, url);

        return ResponseEntity.ok(entity);

    }









    @PutMapping("/posts/{postId}/update")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
                                        @RequestParam(value = "titre", required = false) Optional<String> titre,
                                        @RequestParam(name = "imagecouverture", required = false) Optional<MultipartFile> imagecouverture,
                                        @RequestParam(name = "url", required = false) Optional<MultipartFile> url) throws JsonProcessingException {

        if ((titre.isEmpty() || titre.get().length() <= 0) &&
                (imagecouverture.isEmpty() || imagecouverture.get().getSize() <= 0) &&
                (url.isEmpty() || url.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = titre.isEmpty() ? null : titre.get();
        MultipartFile postPhotoToAdd = imagecouverture.isEmpty() ? null : imagecouverture.get();
        MultipartFile urlAdd = url.isEmpty() ? null : url.get();

        Video updatePost = videoService.updatePost(postId, contentToAdd, postPhotoToAdd,urlAdd);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        videoService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        PostResponse foundPostResponse = videoService.getPostResponseById(postId);
        return new ResponseEntity<>(foundPostResponse, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable("postId") Long postId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Video targetPost = videoService.getPostById(postId);
        List<User> postLikerList = userService.getLikesByPostPaginate(targetPost, page, size);
        return new ResponseEntity<>(postLikerList, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/shares")
    public ResponseEntity<?> getPostShares(@PathVariable("postId") Long postId,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Video sharedPost = videoService.getPostById(postId);
        List<PostResponse> foundPostShares = videoService.getPostSharesPaginate(sharedPost, page, size);
        return new ResponseEntity<>(foundPostShares, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId) {
        videoService.likePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable("postId") Long postId) {
        videoService.unlikePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable("postId") Long postId,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Video targetPost = videoService.getPostById(postId);
        List<CommentResponse> postCommentResponseList = commentService.getPostCommentsPaginate(targetPost, page, size);
        return new ResponseEntity<>(postCommentResponseList, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<?> createPostComment(@PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String contenue) {
        Commentaire savedComment = videoService.createPostComment(postId, contenue);
        CommentResponse commentResponse = CommentResponse.builder()
                .commentaire(savedComment)
                .likedByAuthUser(false)
                .build();
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/update")
    public ResponseEntity<?> updatePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String content) {
        Commentaire savedComment = videoService.updatePostComment(commentId, postId, content);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deletePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId) {
        videoService.deletePostComment(commentId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/comments/{commentId}/like")
    public ResponseEntity<?> likePostComment(@PathVariable("commentId") Long commentId) {
        commentService.likeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/posts/comments/{commentId}/unlike")
    public ResponseEntity<?> unlikePostComment(@PathVariable("commentId") Long commentId) {
        commentService.unlikeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/posts/comments/{commentId}/likes")
    public ResponseEntity<?> getCommentLikeList(@PathVariable("commentId") Long commentId,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Commentaire targetComment = commentService.getCommentaireById(commentId);
        List<User> commentLikes = userService.getLikesByCommentPaginate(targetComment, page, size);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}/share/create")
    public ResponseEntity<?> createPostShare(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "content", required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Video postShare = videoService.createPostShare(contentToAdd, postId);
        return new ResponseEntity<>(postShare, HttpStatus.OK);
    }

    @PostMapping("/posts/{postShareId}/share/update")
    public ResponseEntity<?> updatePostShare(@PathVariable("postShareId") Long postShareId,
                                             @RequestParam(value = "content", required = false) Optional<String> content) {
        String contentToAdd = content.isEmpty() ? null : content.get();
        Video updatedPostShare = videoService.updatePostShare(contentToAdd, postShareId);
        return new ResponseEntity<>(updatedPostShare, HttpStatus.OK);
    }

    @PostMapping("/posts/{postShareId}/share/delete")
    public ResponseEntity<?> deletePostShare(@PathVariable("postShareId") Long postShareId) {
        videoService.deletePostShare(postShareId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/all")
    public List<Video> lister(){
        return videoRepository.findAll();
    }



}
