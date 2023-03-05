package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Plats;
import com.bezkoder.spring.login.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatsRepository extends JpaRepository<Plats,Long> {
        Optional<Plats> findById(Long id);
        List<Plats> findPlatsByAuthor(User author);

        List<Plats> findPlatsByCategorie(Categorie categorie);

@Query(value = "SELECT * FROM `plats` WHERE etat=1; "
        ,nativeQuery = true)
    List<Plats>ListeDesPlats();
        }
