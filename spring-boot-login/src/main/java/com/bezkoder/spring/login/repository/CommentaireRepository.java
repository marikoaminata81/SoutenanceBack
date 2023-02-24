package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    List<Commentaire> findByVideo(Video video,Pageable pageable);
}
