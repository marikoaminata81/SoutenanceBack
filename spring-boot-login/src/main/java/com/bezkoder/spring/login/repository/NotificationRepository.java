package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Optional<Notification> findByReceiverAndOwningVideoAndType(User receiver, Video owningVideo, String type);
    Optional<Notification> findByReceiverAndOwningCommandeAndType(User receiver, Commande owningCommande, String type);
    List<Notification> findNotificationsByReceiver(User receiver,Pageable pageable);

    List<Notification> findNotificationsByReceiverAndIsSeenIsFalse(User receiver);

    List<Notification> findNotificationsByReceiverAndIsReadIsFalse(User receiver);

    void deleteNotificationByOwningVideo(Video owningVideo);

    void deleteNotificationByOwningCommentaire(Commentaire owningCommentaire);
}