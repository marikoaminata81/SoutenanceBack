package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.EmptyCommentException;
import com.bezkoder.spring.login.exception.InvalidOperationException;
import com.bezkoder.spring.login.exception.PostNotFoundException;
import com.bezkoder.spring.login.exception.UserNotFoundException;
import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.payload.response.PostResponse;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.repository.VideoRepository;
import com.bezkoder.spring.login.payload.util.FileNamingUtil;
import com.bezkoder.spring.login.payload.util.FileUploadUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final CommentaireService commentService;
    // private final TagService tagService;
    private final NotificationService notificationService;
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    private final UserRepository userRepository;

    @Override
    public Video getPostById(Long postId) {
        return videoRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public PostResponse getPostResponseById(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Video foundPost = getPostById(postId);
        return PostResponse.builder()
                .video(foundPost)
                .likedByAuthUser(foundPost.getLikeList().contains(authUser))
                .build();
    }

    @Override
    public List<PostResponse> getTimelinePostsPaginate(Integer page, Integer size) {
        User authUser = userService.getAuthenticatedUser();
        List<User> followingList = authUser.getFollowingUsers();
        followingList.add(authUser);
        List<Long> followingListIds = followingList.stream().map(User::getId).toList();
        return videoRepository.findVideoByAuthorIdIn(
                        followingListIds,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostSharesPaginate(Video sharedPost, Integer page, Integer size) {
        return videoRepository.findVideoBySharedPost(
                        sharedPost,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

  /*  @Override
    public List<PostResponse> getPostByTagPaginate(Tag tag, Integer page, Integer size) {
        return videoRepository.findVideoByPostTags(
                        tag,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }*/

    @Override
    public List<PostResponse> getPostsByUserPaginate(User author, Integer page, Integer size) {
        return videoRepository.findVideoByAuthor(
                        author,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public Video createNewPost(String titre, MultipartFile imagecouverture, MultipartFile url) {
       /* User authUser = userService.getAuthenticatedUser();
        Video newPost = new Video();
        newPost.setTitre(titre);
        newPost.setAuthor(authUser);
        newPost.setLikeCount(0);
        newPost.setShareCount(0);
        newPost.setCommentCount(0);
        newPost.setIsTypeShare(false);
        newPost.setSharedPost(null);
        newPost.setDateCreated(new Date());
        newPost.setDateLastModified(new Date());
        return videoRepository.save(newPost);*/
       /* if (imagecouverture != null && imagecouverture.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String newPhotoName = fileNamingUtil.nameFile(imagecouverture);
            String newVideoName = fileNamingUtil.nameFile(url);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;




            try {
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, imagecouverture);

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        if(url != null && url.getSize() >0){
            String uploadDir1 = environment.getProperty("upload.post.video");
            String newVideoName = fileNamingUtil.nameFile(url);
            String newVideoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.video") + File.separator + newVideoName;
            newPost.setUrl(newVideoUrl);

            try {
                fileUploadUtil.saveNewFile(uploadDir1, newVideoName, url);
            } catch (IOException e) {
                throw new RuntimeException();
            }*/
   // }

       /* if (postTags != null && postTags.size() > 0) {
            postTags.forEach(tagDto -> {
                Tag tagToAdd = null;
                try {
                    Tag existingTag = tagService.getTagByName(tagDto.getTagName());
                    if (existingTag != null) {
                        tagToAdd = tagService.increaseTagUseCounter(tagDto.getTagName());
                    }
                } catch (TagNotFoundException e) {
                    tagToAdd = tagService.createNewTag(tagDto.getTagName());
                }
                newPost.getPostTags().add(tagToAdd);
            });
        }*/

return null;
    }

    @Override
    public Video updatePost(Long postId, String titre, MultipartFile imagecouverture,MultipartFile url) {
        Video targetPost = getPostById(postId);
        if (StringUtils.isNotEmpty(titre)) {
            targetPost.setTitre(titre);
        }

        if (imagecouverture != null && imagecouverture.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String oldPhotoName = getPhotoNameFromPhotoUrl(targetPost.getImagecouverture());
            String newPhotoName = fileNamingUtil.nameFile(imagecouverture);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            targetPost.setImagecouverture(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, imagecouverture);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, imagecouverture);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }


        return videoRepository.save(targetPost);
    }

    @Override
    public void deletePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPost = getPostById(postId);

        if (targetPost.getAuthor().equals(authUser)) {
            targetPost.getShareList().forEach(sharingPost -> {
                sharingPost.setSharedPost(null);
                videoRepository.save(sharingPost);
            });

            notificationService.deleteNotificationByOwningPost(targetPost);

            videoRepository.deleteById(postId);

            if (targetPost.getImagecouverture() != null && targetPost.getUrl() !=null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetPost.getImagecouverture());
                String uploadDir1 = environment.getProperty("upload.post.video");
                String VideoName = getPhotoNameFromPhotoUrl(targetPost.getUrl());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);
                    fileUploadUtil.deleteFile(uploadDir1, VideoName);
                } catch (IOException ignored) {}
            }
        } else {
            throw new InvalidOperationException();
        }
    }



    @Override
    public void likePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPost = getPostById(postId);
        if (!targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikeCount(targetPost.getLikeCount() + 1);
            targetPost.getLikeList().add(authUser);
            videoRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetPost.getAuthor(),
                        authUser,
                        targetPost,
                        null,
                        NotificationType.POST_LIKE.name()
                );
            }
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void unlikePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPost = getPostById(postId);
        if (targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikeCount(targetPost.getLikeCount()-1);
            targetPost.getLikeList().remove(authUser);
            videoRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.removeNotification(
                        targetPost.getAuthor(),
                        targetPost,
                        NotificationType.POST_LIKE.name()
                );
            }
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Commentaire createPostComment(Long postId, String contenue) {
        if (StringUtils.isEmpty(contenue)) throw new EmptyCommentException();

        User authUser = userService.getAuthenticatedUser();
        Video targetPost = getPostById(postId);
        Commentaire savedComment = commentService.createNewCommentaire(contenue, targetPost);
        targetPost.setCommentCount(targetPost.getCommentCount()+1);
        videoRepository.save(targetPost);

        if (!targetPost.getAuthor().equals(authUser)) {
            notificationService.sendNotification(
                    targetPost.getAuthor(),
                    authUser,
                    targetPost,
                    savedComment,
                    NotificationType.POST_COMMENT.name()
            );
        }

        return savedComment;
    }

    @Override
    public Commentaire updatePostComment(Long commentId, Long postId, String contenue) {
        if (StringUtils.isEmpty(contenue)) throw new EmptyCommentException();

        return commentService.updateComment(commentId, contenue);
    }

    @Override
    public void deletePostComment(Long commentId, Long postId) {
        Video targetPost = getPostById(postId);
        commentService.deleteComment(commentId);
        targetPost.setCommentCount(targetPost.getCommentCount()-1);
        videoRepository.save(targetPost);
    }

    @Override
    public Video createPostShare(String titre, Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPost = getPostById(postId);
        if (!targetPost.getIsTypeShare()) {
            Video newPostShare = new Video();
            newPostShare.setTitre(titre);
            newPostShare.setAuthor(authUser);
            newPostShare.setLikeCount(0);
            newPostShare.setShareCount(null);
            newPostShare.setCommentCount(0);
            newPostShare.setImagecouverture(null);
            newPostShare.setUrl(null);
            newPostShare.setIsTypeShare(true);
            newPostShare.setSharedPost(targetPost);

            Video savedPostShare = videoRepository.save(newPostShare);
            targetPost.getShareList().add(savedPostShare);
            targetPost.setShareCount(targetPost.getShareCount()+1);
            videoRepository.save(targetPost);

            if (!targetPost.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetPost.getAuthor(),
                        authUser,
                        targetPost,
                        null,
                        NotificationType.POST_SHARE.name()
                );
            }

            return savedPostShare;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Video updatePostShare(String titre, Long postShareId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPostShare = getPostById(postShareId);
        if (targetPostShare.getAuthor().equals(authUser)) {
            targetPostShare.setTitre(titre);
            return videoRepository.save(targetPostShare);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deletePostShare(Long postShareId) {
        User authUser = userService.getAuthenticatedUser();
        Video targetPostShare = getPostById(postShareId);
        if (targetPostShare.getAuthor().equals(authUser)) {
            Video sharedPost = targetPostShare.getSharedPost();
            sharedPost.getShareList().remove(targetPostShare);
            sharedPost.setShareCount(sharedPost.getShareCount()-1);
            videoRepository.save(sharedPost);
            videoRepository.deleteById(postShareId);

            notificationService.deleteNotificationByOwningPost(targetPostShare);
        } else {
            throw new InvalidOperationException();
        }
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }

    private PostResponse postToPostResponse(Video video) {
        User authUser = userService.getAuthenticatedUser();
        return PostResponse.builder()
                .video(video)
                .likedByAuthUser(video.getLikeList().contains(authUser))
                .build();
    }

    public void saveMedia(String titre , MultipartFile imagecouverture, MultipartFile url) throws IOException {
        User user = userService.getAuthenticatedUser(); // récupérer l'utilisateur connecté
// Enregistrer la photo
        String photoPath = saveFile(imagecouverture);

        // Enregistrer la vidéo
        String VideoPath = saveFile(url);

        // Enregistrer le média dans la base de données
        Video media = new Video();
        media.setTitre(titre);
        media.setImagecouverture(photoPath);
        media.setUrl(VideoPath);
        media.setAuthor(user);
        videoRepository.save(media);
    }


    public Video saveVideoAndPhoto(String titre , MultipartFile imagecouverture, MultipartFile url) throws IOException {
        // Enregistrer la photo
        String photoPath = saveFile(imagecouverture);

        // Enregistrer la vidéo
        String VideoPath = saveFile(url);

        User user = userService.getAuthenticatedUser();

        // Créer une nouvelle vidéo
        Video video = new Video();
        video.setTitre(titre);
        video.setImagecouverture(photoPath);
        video.setUrl(VideoPath);
        video.setAuthor(user);
        video.setLikeCount(0);
        video.setCommentCount(0);
        video.setShareCount(0);
        video.setIsTypeShare(false);

        // Enregistrer la vidéo dans la base de données
        videoRepository.save(video);

        // Ajouter la vidéo à la liste de vidéos de l'utilisateur
        user.getVideoList().add(video);
        userRepository.save(user);

        return video;
    }

    // Cette méthode permet d'enregistrer un fichier sur le disque et de renvoyer le chemin du fichier enregistré
    private String saveFile(MultipartFile file) throws IOException {
        // Générer un nom de fichier unique pour éviter les conflits
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path filePath = Paths.get("/path/to/upload/directory/" + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }
}