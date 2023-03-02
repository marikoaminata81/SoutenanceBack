package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.PostNotFoundException;
import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.CategorieRepository;
import com.bezkoder.spring.login.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService{

    private final CategorieRepository  categorieRepository;

    @Override
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();

    }

    @Override
    public Categorie getCategoryById(Long categorieId) {
        return categorieRepository.findById(categorieId).orElseThrow(PostNotFoundException::new);

    }



    private final UserService userService;

    public Categorie createNewCategorie(String nom , MultipartFile imagecouverture) throws IOException {
        // Enregistrer la photo
        String photoPath = saveFile(imagecouverture);


        User user = userService.getAuthenticatedUser();
        System.err.println(user.getId());
        System.err.println(user.getNom());
        System.err.println(user);
        // Créer une nouvelle vidéo
        Categorie categorie = new Categorie();

        categorie.setNom(nom);
        categorie.setImagecouverture(photoPath);
        // Enregistrer la vidéo dans la base de données
        categorieRepository.save(categorie);

        // Ajouter la vidéo à la liste de vidéos de l'utilisateur

        //userRepository.save(user);

        return categorie;
    }






    // Cette méthode permet d'enregistrer un fichier sur le disque et de renvoyer le chemin du fichier enregistré
    private String saveFile(MultipartFile file) throws IOException {
        // String filePath = "C:/Users/ammariko/Documents/ionic/ikaGaFront/src/assets" + file.getOriginalFilename();
        String filePath = "C:/Users/Poste7/Documents/SoutenanceFront-2/src/assets" + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);
        return filePath;
    }



}
