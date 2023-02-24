package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.services.UserService;
import com.bezkoder.spring.login.security.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/User")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
     UserService userService;



    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  String delete(@PathVariable Long id){
        //this.userService.supprimer(id);
         userRepository.deleteById(id);
        return "Utilisateur supprimer avec success";
    }

    @PostMapping("/creer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    User add(@RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping("/modifier/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User update(@RequestBody User user, @PathVariable Long id){

        return userService.modifier(user,id);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<User> lister(){
        return userRepository.findAll();
    }



}
