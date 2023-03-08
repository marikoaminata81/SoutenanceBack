package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;

import com.bezkoder.spring.login.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ProduitService {
    Produit getProduitById(Long prodId);

   // PostResponse getPostResponseById(Long prodId);

   List<Produit>ListeDesProduit();
    List<Produit> getProduitByUser(User author);


    Produit createNewProduit(String nom , MultipartFile imagecouverture, Double prix, String reference , String description, Date datefab, Date dateperem, Categorie categorie) throws IOException;
    Produit updateProduit(Long prodId, String nom, MultipartFile imagecouverture,String description);
    void deleteProduit(Long prodId);


}


