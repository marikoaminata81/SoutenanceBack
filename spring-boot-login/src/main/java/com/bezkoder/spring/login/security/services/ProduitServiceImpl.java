package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.EmptyCommentException;
import com.bezkoder.spring.login.exception.InvalidOperationException;
import com.bezkoder.spring.login.exception.PostNotFoundException;
import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.payload.response.PostResponse;
import com.bezkoder.spring.login.payload.util.FileNamingUtil;
import com.bezkoder.spring.login.payload.util.FileUploadUtil;
import com.bezkoder.spring.login.repository.ProduitRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class ProduitServiceImpl implements ProduitService {
    private final ProduitRepository produitRepository;
    private final UserService userService;


    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    private final UserRepository userRepository;

    @Override
    public Produit getProduitById(Long prodId) {
        return produitRepository.findById(prodId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public List<Produit> ListeDesProduit() {
        return produitRepository.ListeDesProduit();
    }

    @Override
    public List<Produit> getProduitByUser(User author) {
        return produitRepository. findProduitByAuthor(author);
    }




    @Override
    public Produit updateProduit(Long prodId, String nom, MultipartFile imagecouverture,String description) {
        Produit targetProd = getProduitById(prodId);
        if (StringUtils.isNotEmpty(nom)) {
            targetProd.setNom(nom);
        }

        if (imagecouverture != null && imagecouverture.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String oldPhotoName = getPhotoNameFromPhotoUrl(targetProd.getImagecouverture());
            String newPhotoName = fileNamingUtil.nameFile(imagecouverture);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            targetProd.setImagecouverture(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, imagecouverture);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, imagecouverture);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }


        return produitRepository.save(targetProd);
    }

    @Override
    public void deleteProduit(Long prodId) {
        User authUser = userService.getAuthenticatedUser();
        Produit targetProd = getProduitById(prodId);

            produitRepository.deleteById(prodId);

            if (targetProd.getImagecouverture() != null && targetProd. getImagecouverture() !=null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetProd.getImagecouverture());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);

                } catch (IOException ignored) {}
            }

}


    public List<Produit> getAllProducts() {
        return produitRepository.findAll();
    }

    public Produit getProductById(Long productId) {
        return produitRepository.findById(productId).orElseThrow(PostNotFoundException::new);
    }

    public List<Produit> getProductsByCategory(Categorie categorie) {
        return produitRepository.findProduitByCategorie(categorie);
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }

    private PostResponse postToPostResponse(Video video) {
        User authUser = userService.getAuthenticatedUser();
        return PostResponse.builder()
                .video(video)
                .likedByAuthUser(video.getLikeList().contains(authUser))
                .build();
    }

    public Produit modifier(Produit produit, Long id) {

        return produitRepository.findById(id)
                .map(u ->{
                    u.setEtat(produit.isEtat());

                    return produitRepository.save(u);
                } ).orElseThrow(() -> new RuntimeException("Ce produit n'existe pas !"));
    }

    public Produit createNewProduit(String nom , MultipartFile imagecouverture,Double prix, String reference ,String description,Date datefab,Date dateperem, Categorie categorie) throws IOException {
        // Enregistrer la photo
        String photoPath = saveFile(imagecouverture);


        User user = userService.getAuthenticatedUser();
        System.err.println(user.getId());
        System.err.println(user.getNom());
        System.err.println(user);
        // Créer une nouvelle vidéo
        Produit produit = new Produit();

        produit.setNom(nom);
        produit.setImagecouverture(photoPath);
        produit.setDescription(description);
        produit.setReference(reference);
        produit.setPrix(prix);
        produit.setDatefab(datefab);
        produit.setDateperem(dateperem);
        produit.setEtat(false);
      //  produit.setQuantiteVente(quantiteVente);
        produit.setCategorie(categorie);
        produit.setAuthor(user);
        // Enregistrer la vidéo dans la base de données
        produitRepository.save(produit);

        // Ajouter la vidéo à la liste de vidéos de l'utilisateur

        //userRepository.save(user);

        return produit;
    }






    // Cette méthode permet d'enregistrer un fichier sur le disque et de renvoyer le chemin du fichier enregistré
    private String saveFile(MultipartFile file) throws IOException {
         String filePath = "C:/Users/ammariko/Documents/ionic/ikaGaFront/src/assets" + file.getOriginalFilename();
        //String filePath = "C:/Users/Poste7/Documents/SoutenanceFront-2/src/assets" + file.getOriginalFilename();
        File dest = new File(filePath);
        file.transferTo(dest);
        return file.getOriginalFilename();
    }

}
