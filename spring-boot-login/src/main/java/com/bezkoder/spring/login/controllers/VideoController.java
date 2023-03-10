package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.exception.EmptyPostException;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.CommentResponse;
import com.bezkoder.spring.login.repository.CommentaireRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.repository.VideoRepository;
import com.bezkoder.spring.login.security.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin
@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final VideoServiceImpl videoServiceImpl;
    private final CommentaireService commentService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final CommentaireRepository commentaireRepository;
    private final NotificationService notificationService;

    @PostMapping("/posts/create")

    public void uploadVideoAndPhoto(
            @RequestParam("titre") String titre,
            @RequestParam("imagecouverture") MultipartFile imagecouverture,
            @RequestParam("url") MultipartFile url

    ) throws IOException {

        try {

            videoServiceImpl.saveVideoAndPhoto(titre, imagecouverture, url);

        } catch (IOException e) {
           System.err.println(e);
        }
    }


    /*
        @PostMapping("/posts/create")

    public ResponseEntity<String> uploadVideoAndPhoto(
            @RequestParam("titre") String titre,
            @RequestParam("imagecouverture") MultipartFile imagecouverture,
            @RequestParam("url") MultipartFile url

    ) throws IOException {

        try {
            videoServiceImpl.saveVideoAndPhoto(titre, imagecouverture, url);
            return ResponseEntity.ok("Vid??o publi?? avec Succes!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Publication ??chou??e");
        }
    }
     */









    @PostMapping("/posts/{postId}/update")
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
//marche pas
    @PostMapping("/posts/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        videoService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> supprimerVideo(@PathVariable("postId") Long postId) {

        Video targetPost = videoService.getPostById(postId);
        User UserAuth = userService.getAuthenticatedUser();
        if (targetPost.getAuthor().equals(UserAuth)){
            videoRepository.deleteById(postId);
        }

        return new ResponseEntity<>(postId, HttpStatus.OK);
    }

//??a marche
    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        Video foundPostResponse = videoService.getPostById(postId);
        return new ResponseEntity<>(foundPostResponse, HttpStatus.OK);
    }
//??a marche
    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable("postId") Long postId) {

        Video targetPost = videoService.getPostById(postId);
        List<User> postLikerList = userService.getLikesByPostPaginate(targetPost);
        return new ResponseEntity<>(postLikerList, HttpStatus.OK);
    }

//pas important
    @GetMapping("/posts/{postId}/shares")
    public ResponseEntity<?> getPostShares(@PathVariable("postId") Long postId) {

        Video sharedPost = videoService.getPostById(postId);
        List<Video> foundPostShares = videoService.getPostSharesPaginate(sharedPost);
        return new ResponseEntity<>(foundPostShares, HttpStatus.OK);
    }
//??a marche
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId) {
        videoService.likePost(postId);
        return  ResponseEntity.ok("Vid??o lik?? avec Succes!");
    }
//??a marche
    @PostMapping("/posts/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable("postId") Long postId) {
        videoService.unlikePost(postId);
        return ResponseEntity.ok("Vid??o d??lik?? avec Succes!");
    }
//??a marche
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable("postId") Long postId) {

        Video targetPost = videoService.getPostById(postId);
        List<CommentResponse> postCommentResponseList = commentService.getPostCommentsPaginate(targetPost);
        return new ResponseEntity<>(postCommentResponseList, HttpStatus.OK);
    }

    /*@PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<?> createPostComment(@PathVariable("postId") Long postId,
                                               @RequestParam(value = "contenue") String contenue)  {


         System.out.println("gfhjklm"+contenue);
            Commentaire savedComment = videoService.createPostComment(postId, contenue);
            CommentResponse commentResponse = CommentResponse.builder()
                    .commentaire(savedComment)
                    .likedByAuthUser(false)
                    .build();

            return new ResponseEntity<>(commentResponse, HttpStatus.OK);

    }*/

    @PostMapping("/posts/{postId}/comments/{commentId}/update")
    public ResponseEntity<?> updatePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId,
                                               @RequestParam(value = "contenue") String contenue) {
        Commentaire com = new Commentaire();
        com.setContenue(contenue);
        commentaireRepository.save(com);
        Commentaire savedComment = videoService.updatePostComment(commentId, postId, contenue);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }
//??a marche
    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deletePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId) {
        videoService.deletePostComment(commentId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //??a marche
    @PostMapping("/posts/comments/{commentId}/like")
    public ResponseEntity<?> likePostComment(@PathVariable("commentId") Long commentId) {
        commentService.likeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //??a marche
    @PostMapping("/posts/comments/{commentId}/unlike")
    public ResponseEntity<?> unlikePostComment(@PathVariable("commentId") Long commentId) {
        commentService.unlikeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//??a marche
    @GetMapping("/posts/comments/{commentId}/likes")
    public ResponseEntity<?> getCommentLikeList(@PathVariable("commentId") Long commentId) {

        Commentaire targetComment = commentService.getCommentaireById(commentId);
        List<User> commentLikes = userService.getLikesByCommentPaginate(targetComment);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }


    @PostMapping("/posts/{postId}/share/create")
    public ResponseEntity<?> createPostShare(@PathVariable("postId") Long postId,
                                             @RequestParam(value = "contenue", required = false) Optional<String> contenue) {
        String contentToAdd = contenue.isEmpty() ? null : contenue.get();
        Video postShare = videoService.createPostShare(contentToAdd, postId);
        return new ResponseEntity<>(postShare, HttpStatus.OK);
    }

    @PostMapping("/posts/{postShareId}/share/update")
    public ResponseEntity<?> updatePostShare(@PathVariable("postShareId") Long postShareId,
                                             @RequestParam(value = "contenue", required = false) Optional<String> contenue) {
        String contentToAdd = contenue.isEmpty() ? null : contenue.get();
        Video updatedPostShare = videoService.updatePostShare(contentToAdd, postShareId);
        return new ResponseEntity<>(updatedPostShare, HttpStatus.OK);
    }
//Pas important
//??a marche pas
    @PostMapping("/posts/{postShareId}/share/delete")
    public ResponseEntity<?> deletePostShare(@PathVariable("postShareId") Long postShareId) {
        videoService.deletePostShare(postShareId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/posts/{postId}/{userId}")
    public boolean verifierUserLikePost(@PathVariable("postId") Long postId,@PathVariable("userId") Long userId) {

        return videoService.verifyLikeByUser(postId,userId);
    }

    //??a marche
    @GetMapping("/all")
    public List<Video> lister(){
        return videoRepository.findAll();
    }





   //??a marche
    @PostMapping("/posts/{postId}/comments")
    public String CreaCommen(@PathVariable("postId") Long postId,
                                   @RequestBody Commentaire com
                                   //@RequestBody String contenue
                                    ) {


      //Commentaire com = new Commentaire();
       //com.setContenue(contenue);
       Long user = userService.getAuthenticatedUser().getId();
       User us = userRepository.findById(user).get();
        User user1 = userRepository.findById(user).get();
       com.setAuthor(us);
       Video vide = videoRepository.findById(postId).get();
       //vide.setCommentCount();
       com.setVideo(vide);
       com.setLikeCount(0);
       com.setDateCreated(new Date());
       com.setDateLastModified(new Date());

       //String ccc = commentaireRepository.findByContenue(contenue);
        videoService.createPostComment(postId, com.getContenue());
        //notificationService.sendNotification(us,);
       //commentaireRepository.save(com);
       return "Vid??o comment?? avec succ??es";
    }



  /*  @PostMapping("/posts/{postId}/comments/creation")
    public void Commentaire(@PathVariable("postId") Long postId,
                            @RequestBody Commentaire com
                          //  @RequestBody String contenue

    ) throws IOException {

        //Commentaire com = new Commentaire();
        // com.setContenue(contenue);
        Long user = userService.getAuthenticatedUser().getId();
        User us = userRepository.findById(user).get();
        com.setContenue(com.getContenue());
        com.setAuthor(us);
        Video vide = videoRepository.findById(postId).get();
        //vide.setCommentCount();
        com.setVideo(vide);
        com.setLikeCount(0);
        com.setDateCreated(new Date());
        com.setDateLastModified(new Date());
        commentaireRepository.save(com);


    }

    @PostMapping("/posts/{postId}/comments/amina")
    public String createPostComment(@PathVariable Long postId,
                                               @RequestBody Commentaire com) {
        Long user = userService.getAuthenticatedUser().getId();
        User us = userRepository.findById(user).get();
        com.setContenue(com.getContenue());
        com.setAuthor(us);
        Video vide = videoRepository.findById(postId).get();
        //vide.setCommentCount();

        com.setVideo(vide);
        com.setLikeCount(0);
        com.setDateCreated(new Date());
        com.setDateLastModified(new Date());
        commentaireRepository.save(com);
        return  "Bravo";
    }*/




    @GetMapping("/videoParUser/{userId}")
    public List<Video> videoParUser(@PathVariable("userId") User userId){

        return videoRepository.findVideoByAuthor(userId);
    }



}
