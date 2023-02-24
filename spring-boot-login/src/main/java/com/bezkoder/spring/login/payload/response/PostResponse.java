package com.bezkoder.spring.login.payload.response;

import com.bezkoder.spring.login.models.Video;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Video video;
    private Boolean likedByAuthUser;
}
