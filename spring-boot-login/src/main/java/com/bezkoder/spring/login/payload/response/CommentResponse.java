package com.bezkoder.spring.login.payload.response;

import com.bezkoder.spring.login.models.Commentaire;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Commentaire commentaire;
    private Boolean likedByAuthUser;
}
