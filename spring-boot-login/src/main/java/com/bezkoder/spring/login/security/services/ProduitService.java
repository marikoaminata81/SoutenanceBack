package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;

import com.bezkoder.spring.login.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProduitService {
    Produit getProduitById(Long prodId);

   // PostResponse getPostResponseById(Long prodId);

   List<Produit>ListeDesProduitValider(Produit produit);
    List<Produit> getProduitByUser(User author);


    Produit createNewProduit(String nom , MultipartFile imagecouverture,Double prix, String reference ,String description, Categorie categorie) throws IOException;
    Produit updateProduit(Long prodId, String nom, MultipartFile imagecouverture,String description);
    void deleteProduit(Long prodId);


}


