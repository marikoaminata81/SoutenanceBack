package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Plats;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PlatsService {
    Plats getPlatsById(Long platId);

    // PostResponse getPostResponseById(Long prodId);

    List<Plats> ListeDesPlats();
    List<Plats> getPlatsByUser(User author);


    Plats createNewPlats(String nom , MultipartFile imagecouverture, Double prix, String reference , String description, Categorie categorie) throws IOException;
    Plats updatePlats(Long platId, String nom, MultipartFile imagecouverture,String description);
    void deletePlats(Long platId);

}
