package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.security.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
//@CrossOrigin
@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/v1/categorie")
public class CategorieController {

    private final CategorieService categorieService;
    private final CategorieServiceImpl categorieServiceImpl;


    @PostMapping("/create")
    public void createNewCategorie(
            @RequestParam("nom") String nom,
            @RequestParam("imagecouverture") MultipartFile imagecouverture


    ) throws IOException {

        try {

            categorieServiceImpl.createNewCategorie(nom, imagecouverture);

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
