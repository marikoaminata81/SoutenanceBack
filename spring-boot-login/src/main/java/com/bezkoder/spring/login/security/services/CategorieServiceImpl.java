package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produits;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieServiceImpl implements CategorieService{
    @Autowired
    private CategorieRepository categorieRepository;


    @Override
    public MessageResponse Ajouter(Categorie categorie, User user) {
        if(categorieRepository.findByNomcat(categorie.getNomcat()) == null){
            categorie.setUser(user);

            categorieRepository.save(categorie);
            MessageResponse message = new MessageResponse("Produit créer avec succès",true);
            return message;
        }
        else{
            MessageResponse message = new MessageResponse("Cette reference de prodits  existe déjà !",false);
            return message;
        }

    }

    @Override
    public MessageResponse Modifier(Categorie categorie, Long idcat) {
        Optional<Categorie> categorie1 = categorieRepository.findById(idcat);
        if(!categorie1.isPresent()){

            MessageResponse message = new MessageResponse("Cette catégorie est introuvable !", false);
            return message;
        }
        else {

            Categorie categorie2 = categorieRepository.findById(idcat).get();
            categorie2.setNomcat(categorie.getNomcat());
            categorie2.setImage(categorie.getImage());

            this.categorieRepository.save(categorie2);

            MessageResponse message = new MessageResponse("Catégorie modifiée avec succès !", true);
            return message;


        }
    }

    @Override
    public MessageResponse Supprimer(Long idcat) {
        Optional<Categorie> categorie = this.categorieRepository.findById(idcat);
        if (!categorie.isPresent())
        {
            MessageResponse message = new MessageResponse("Catégorie introuvable !", false);
            return message;
        }
        else {
            this.categorieRepository.delete(categorie.get());
            MessageResponse message = new MessageResponse("Catégorie supprimé avec succès !", true);
            return message;
        }
    }

    @Override
    public List<Categorie> Lister() {
        return this.categorieRepository.findAll();
    }
}
