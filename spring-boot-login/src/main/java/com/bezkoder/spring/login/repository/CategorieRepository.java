package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {

    Optional<Categorie> findById(Long id);

    @Query(value = "select * from categorie"
            ,nativeQuery = true)
    List<Categorie> ListeDesCategorie(Categorie categorie);
}
