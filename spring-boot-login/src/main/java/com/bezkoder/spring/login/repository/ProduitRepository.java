package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
    Optional<Produit> findById(Long id);
    List<Produit> findProduitByAuthor(User author);

    List<Produit> findProduitByCategorie(Categorie categorie);

    @Query(value = "SELECT * FROM `produit` WHERE etat=1; "
            ,nativeQuery = true)
    List<Produit>ListeDesProduit();
}
