package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produit;

import java.util.List;

public interface CategorieService {
    public List<Categorie> getAllCategories();


    public Categorie getCategoryById(Long categorieId) ;


}
