package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.jwt.EmailConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService{
    /*@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
*/
    @Autowired
    UserRepository userRepository;
    @Override
    public List<User> lister() {
        return userRepository.findAll();
    }

    @Override
    public User creer(User user) {
        return userRepository.save(user);
    }


    @Override
    public User modifier(User user, Long id) {

        return userRepository.findById(id)
                .map(u ->{
                    u.setNom(user.getNom());
                    u.setPrenom(user.getPrenom());
                    u.setUsername(user.getUsername());
                    u.setNumero(user.getNumero());
                    u.setEmail(user.getEmail());
                    u.setPassword(user.getPassword());
                    u.setAdresse(user.getAdresse());
                    u.setPhoto(user.getPhoto());
                    return userRepository.save(u);
                } ).orElseThrow(() -> new RuntimeException("Cet utilisateur n'existe pas !"));
    }

    @Override
    public String supprimer(Long id) {
        userRepository.deleteById(id);
        return "Utilisateur supprimé avec succès";
    }

    @Override
    public MessageResponse ModifierProfil(User user, Long iduser) {
        if(userRepository.findById(iduser) != null){
            User utilisateur = userRepository.findById(iduser).get();

            utilisateur.setPhoto(user.getPhoto());
            userRepository.save(utilisateur);
            MessageResponse message = new MessageResponse("Photo de profil modifiée avec succès !",true);
            return message;
        }
        else{

            MessageResponse message = new MessageResponse("Photo de profil introuvable !",true);
            return message;

        }
    }

    @Override
    public void resetPassword(User user) {

    }

    @Override
    public void updateUserPassword(User user, String newPassword) {

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
