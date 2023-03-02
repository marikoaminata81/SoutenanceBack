package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Historique;
import com.bezkoder.spring.login.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique,Long> {

    List<Historique> findByUser(User user);


}
