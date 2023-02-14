package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> lister();

    User creer(User user);

    User modifier(User user, Long iduser);

    String supprimer(Long iduser);
    MessageResponse ModifierProfil(User user, Long iduser);

    public void resetPassword(User user);

    public void updateUserPassword(User user, String newPassword);

    User findByEmail(String email);
}
