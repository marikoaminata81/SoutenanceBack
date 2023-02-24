package com.bezkoder.spring.login.payload.response;


import com.bezkoder.spring.login.models.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User user;
    private Boolean followedByAuthUser;
}
