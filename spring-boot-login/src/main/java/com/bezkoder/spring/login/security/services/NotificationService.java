package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.*;

import java.util.List;

public interface NotificationService {
    Notification getNotificationById(Long notificationId);
    Notification getNotificationByReceiverAndOwningPostAndType(User receiver, Video owninVideo, String type);
    void sendNotification(User receiver, User sender, Video owningVideo, Commentaire owningCommentaire, String type);
    void removeNotification(User receiver, Video owningVideo, String type);
    List<Notification> getNotificationsForAuthUserPaginate(Integer page, Integer size);
    void markAllSeen();
    void markAllRead();
    void deleteNotification(User receiver, Video owningVideo, String type);
    void deleteNotificationByOwningPost(Video owningVideo);
    void deleteNotificationByOwningComment(Commentaire owningCommentaire);

    void sendNotificationCommande(User receiver, User sender, Commande commande, String type);
    Notification getNotificationByReceiverAndOwningCommandeAndType(User receiver, Commande commande, String type);
}
