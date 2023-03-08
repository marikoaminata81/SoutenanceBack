package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.repository.CategorieRepository;
import com.bezkoder.spring.login.security.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
//@CrossOrigin
@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/v1/categorie")
public class CategorieController {

    private final CategorieService categorieService;
    private final CategorieServiceImpl categorieServiceImpl;
    private final CategorieRepository categorieRepository;


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


    @GetMapping("/all")

    public List<Categorie> lister(){
        return categorieRepository.findAll();
    }
}