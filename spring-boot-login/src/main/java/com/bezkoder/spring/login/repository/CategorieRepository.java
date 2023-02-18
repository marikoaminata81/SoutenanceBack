package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    Categorie findByNomcat(String nomcat);
}
