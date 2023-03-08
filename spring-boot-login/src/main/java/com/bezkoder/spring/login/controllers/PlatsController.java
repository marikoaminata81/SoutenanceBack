package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.exception.EmptyPostException;
import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Plats;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.PlatsRepository;
import com.bezkoder.spring.login.repository.ProduitRepository;
import com.bezkoder.spring.login.security.services.PlatsService;
import com.bezkoder.spring.login.security.services.PlatsServiceImpl;
import com.bezkoder.spring.login.security.services.ProduitService;
import com.bezkoder.spring.login.security.services.ProduitServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/plat")

@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
public class PlatsController {
    private final PlatsService platsService;
    private final PlatsServiceImpl platsServiceImpl;
    private final PlatsRepository platsRepository;

    //ça marche
    @PostMapping("/create")
    public void createNewPlats(
            @RequestParam("nom") String nom,
            @RequestParam("imagecouverture") MultipartFile imagecouverture,
            @RequestParam("reference") String reference,
            @RequestParam("prix") Double prix,
            @RequestParam("description") String description,

            @RequestParam("categorie") Categorie categorie


    ) throws IOException {

        try {

            platsServiceImpl.createNewPlats(nom ,imagecouverture,prix,  reference ,description,categorie);

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @PostMapping("/{platId}/update")
    public ResponseEntity<?> updatePost(@PathVariable("platId") Long platId,
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

        Plats updatePlats = platsService.updatePlats(platId, nomToAdd, postPhotoToAdd, descriptionAdd);
        return new ResponseEntity<>(updatePlats, HttpStatus.OK);
    }

    //ça marche
    @PutMapping("/modifier/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Plats update(@RequestBody Plats plats, @PathVariable Long id) {
        Plats existingProduct = platsService.getPlatsById(id);
        existingProduct.setEtat(plats.isEtat());
        //  produitServiceImpl.modifier(existingProduct);
        return platsServiceImpl.modifier(plats, id);
    }

    @GetMapping("/platsValider")
    public List<Plats> ListeDesPlats() {
        return platsRepository.ListeDesPlats();
    }

    @GetMapping("/lister")
    public List<Plats> ListePlats(){
        return platsRepository.findAll();
    }

    @GetMapping("/platParUser/{userId}")
    public List<Plats> platParUser(@PathVariable("userId") User userId){

        return platsRepository.findPlatsByAuthor(userId);
    }

    @GetMapping("/platParUser/{id}")
    public List<Plats> platParUserCategorie(@PathVariable("id") Categorie id){

        return platsRepository.findPlatsByCategorie(id);
    }
}