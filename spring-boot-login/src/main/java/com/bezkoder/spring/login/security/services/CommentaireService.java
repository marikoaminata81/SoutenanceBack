package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.payload.response.CommentResponse;

import java.util.List;

public interface CommentaireService {
    Commentaire getCommentaireById(Long commentId);
    Commentaire createNewCommentaire(String contenue, Video video);
    Commentaire updateComment(Long commentId, String contenue);
    Commentaire likeComment(Long commentId);
    Commentaire unlikeComment(Long commentId);
    void deleteComment(Long commentId);
    List<CommentResponse> getPostCommentsPaginate(Video video, Integer page, Integer size);
}
