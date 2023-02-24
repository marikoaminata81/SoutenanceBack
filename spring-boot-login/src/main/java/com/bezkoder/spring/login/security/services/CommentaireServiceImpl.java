package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.CommentNotFoundException;
import com.bezkoder.spring.login.exception.InvalidOperationException;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.NotificationType;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.CommentResponse;
import com.bezkoder.spring.login.repository.CommentaireRepository;
import com.bezkoder.spring.login.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentaireServiceImpl implements CommentaireService{

    private final CommentaireRepository commentRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final VideoRepository videoRepository;

    @Override
    public Commentaire getCommentaireById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Commentaire createNewCommentaire(String contenue, Video video) {
        User authUser = userService.getAuthenticatedUser();
        Commentaire newComment = new Commentaire();
        newComment.setContenue(contenue);
        newComment.setAuthor(authUser);
        newComment.setVideo(video);
        newComment.setLikeCount(0);
        newComment.setDateCreated(new Date());
        newComment.setDateLastModified(new Date());
        return commentRepository.save(newComment);

    }

    @Override
    public Commentaire updateComment(Long commentId, String contenue) {
        User authUser = userService.getAuthenticatedUser();
        Commentaire targetComment = getCommentaireById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            targetComment.setContenue(contenue);
            targetComment.setDateLastModified(new Date());
            return commentRepository.save(targetComment);
        } else {
            throw new InvalidOperationException();
        }
    }
    @Override
    public void deleteComment(Long commentId) {
        User authUser = userService.getAuthenticatedUser();
        Commentaire targetComment = getCommentaireById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            commentRepository.deleteById(commentId);
            notificationService.deleteNotificationByOwningComment(targetComment);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Commentaire likeComment(Long commentId) {
        User authUser = userService.getAuthenticatedUser();
        Commentaire targetComment = getCommentaireById(commentId);
        if (!targetComment.getLikeList().contains(authUser)) {
            targetComment.setLikeCount(targetComment.getLikeCount()+1);
            targetComment.getLikeList().add(authUser);
            targetComment.setDateLastModified(new Date());
            Commentaire updatedComment = commentRepository.save(targetComment);

            if (!targetComment.getAuthor().equals(authUser)) {
                notificationService.sendNotification(
                        targetComment.getAuthor(),
                        authUser,
                        targetComment.getVideo(),
                        targetComment,
                        NotificationType.COMMENT_LIKE.name()
                );
            }

            return updatedComment;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Commentaire unlikeComment(Long commentId) {
        User authUser = userService.getAuthenticatedUser();
        Commentaire targetComment = getCommentaireById(commentId);
        if (targetComment.getLikeList().contains(authUser)) {
            targetComment.setLikeCount(targetComment.getLikeCount() - 1);
            targetComment.getLikeList().remove(authUser);
            targetComment.setDateLastModified(new Date());
            Commentaire updatedComment = commentRepository.save(targetComment);

            if (!targetComment.getAuthor().equals(authUser)) {
                notificationService.removeNotification(
                        targetComment.getVideo().getAuthor(),
                        targetComment.getVideo(),
                        NotificationType.COMMENT_LIKE.name()
                );
            }

            return updatedComment;
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public List<CommentResponse> getPostCommentsPaginate(Video video, Integer page, Integer size) {
        User authUser = userService.getAuthenticatedUser();
        List<Commentaire> foundCommentList = commentRepository.findByVideo(
                video,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated"))
        );

        List<CommentResponse> commentResponseList = new ArrayList<>();
        foundCommentList.forEach(comment -> {
            CommentResponse newCommentResponse = CommentResponse.builder()
                    .commentaire(comment)
                    .likedByAuthUser(comment.getLikeList().contains(authUser))
                    .build();
            commentResponseList.add(newCommentResponse);
        });

        return commentResponseList;
    }
}
