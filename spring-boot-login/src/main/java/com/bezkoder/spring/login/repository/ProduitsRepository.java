package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produits;
import com.bezkoder.spring.login.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ProduitsRepository extends JpaRepository<Produits,Long> {
    public Produits findByRefprod(String refprod);

    public List<Produits> findByUser(User user);

    List<Produits> findByUserAndDatesoumis(User user, LocalDate datesoumis);

    List<Produits> findByUserAndDatefab(User user, Date datefab);

    List<Produits> findByUserAndDateperem(User user, Date dateperem);

    List<Produits> findByCategorie(Categorie categorie);


    //Afficher Les Produits Par Disponibilite
   @Query(value = "SELECT * from produits where disponibilite =: disponibilite", nativeQuery = true)
    public List<Produits> AfficherLesProduitsParDisponibilite(boolean disponibilite);

    @Query(value = "SELECT * FROM produits " +
            "WHERE  disponibilite=true;", nativeQuery = true)
    List<Produits> produitsDisponible();

    @Query(value = "SELECT * FROM produits " +
            "WHERE  disponibilite=false;", nativeQuery = true)
    List<Produits> produitsIndisponible();

    @Query(value = "SELECT * FROM produits " +
            "WHERE  Statut='En attente';", nativeQuery = true)
    List<Produits> produitsEnAttente();

    @Query(value = "SELECT * FROM produits " +
            "WHERE  Statut='Posté';", nativeQuery = true)
    List<Produits> produitsPoste();

    @Query(value = "SELECT * FROM produits " +
            "WHERE  Statut='Rejeté';", nativeQuery = true)
    List<Produits>produitsrejet();

}