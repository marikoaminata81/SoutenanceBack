package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.exception.EmptyPostException;
import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.repository.ProduitRepository;
import com.bezkoder.spring.login.security.services.ProduitService;
import com.bezkoder.spring.login.security.services.ProduitServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produit")
public class ProduitController {

    private final ProduitService produitService;
    private final ProduitServiceImpl produitServiceImpl;
    private final ProduitRepository produitRepository;

    //ça marche
    @PostMapping("/create")
    public void createNewCategorie(
            @RequestParam("nom") String nom,
            @RequestParam("imagecouverture") MultipartFile imagecouverture,
            @RequestParam("reference") String reference,
            @RequestParam("prix") Double prix,
            @RequestParam("description") String description,
           // @RequestParam("quantiteVente") Double quantiteVente,
            @RequestParam("categorie") Categorie categorie


    ) throws IOException {

        try {

            produitServiceImpl.createNewProduit(nom ,imagecouverture,prix,  reference ,description,categorie);

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @PostMapping("/{prodId}/update")
    public ResponseEntity<?> updatePost(@PathVariable("prodId") Long prodId,
                                        @RequestParam(value = "nom", required = false) Optional<String> nom,
                                        @RequestParam(name = "imagecouverture", required = false) Optional<MultipartFile> imagecouverture,
                                        @RequestParam(name = "description", required = false) Optional<String> description) throws JsonProcessingException {

        if ((nom.isEmpty() || nom.get().length() <= 0) &&
                (imagecouverture.isEmpty() || imagecouverture.get().getSize() <= 0) &&
                (description.isEmpty() || description.get().length() <= 0)) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String nomToAdd = nom.isEmpty() ? null : nom.get();
        MultipartFile postPhotoToAdd = imagecouverture.isEmpty() ? null : imagecouverture.get();
        String descriptionAdd = description.isEmpty() ? null : description.get();

        Produit updateProduit = produitService.updateProduit(prodId, nomToAdd, postPhotoToAdd, descriptionAdd);
        return new ResponseEntity<>(updateProduit, HttpStatus.OK);
    }

    //ça marche
    @PutMapping("/modifier/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Produit update(@RequestBody Produit produit, @PathVariable Long id) {
        Produit existingProduct = produitService.getProduitById(id);
        existingProduct.setEtat(produit.isEtat());
        //  produitServiceImpl.modifier(existingProduct);
        return produitServiceImpl.modifier(produit, id);
    }

    @GetMapping("/produitValider")
    public List<Produit> lister(Produit produit) {
        return produitRepository.ListeDesProduit(produit);
    }

}
