package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.exception.NotificationNotFoundException;
import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Notification;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import com.bezkoder.spring.login.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
    }

    @Override
    public Notification getNotificationByReceiverAndOwningPostAndType(User receiver, Video owningVideo, String type) {
        return notificationRepository.findByReceiverAndOwningVideoAndType(receiver, owningVideo, type)
                .orElseThrow(NotificationNotFoundException::new);
    }

    @Override
    public void sendNotification(User receiver, User sender, Video owningVideo, Commentaire owningCommentaire, String type) {
        try {
            Notification targetNotification = getNotificationByReceiverAndOwningPostAndType(receiver, owningVideo, type);
            targetNotification.setSender(sender);
            targetNotification.setIsSeen(false);
            targetNotification.setIsRead(false);
            targetNotification.setDateUpdated(new Date());
            targetNotification.setDateLastModified(new Date());
            notificationRepository.save(targetNotification);
        } catch (NotificationNotFoundException e) {
            Notification newNotification = new Notification();
            newNotification.setType(type);
            newNotification.setReceiver(receiver);
            newNotification.setSender(sender);
            newNotification.setOwningVideo(owningVideo);
            newNotification.setOwningCommentaire(owningCommentaire);
            newNotification.setIsSeen(false);
            newNotification.setIsRead(false);
            newNotification.setDateCreated(new Date());
            newNotification.setDateUpdated(new Date());
            newNotification.setDateLastModified(new Date());
            notificationRepository.save(newNotification);
        }
    }

    @Override
    public void removeNotification(User receiver, Video owningVideo, String type) {
        User authUser = userService.getAuthenticatedUser();
        Notification targetNotification = getNotificationByReceiverAndOwningPostAndType(receiver, owningVideo, type);
        if (targetNotification.getSender() != null && targetNotification.getSender().equals(authUser)) {
            targetNotification.setSender(null);
            targetNotification.setDateLastModified(new Date());
            notificationRepository.save(targetNotification);
        }
    }

    @Override
    public List<Notification> getNotificationsForAuthUserPaginate(Integer page, Integer size) {
        User authUser = userService.getAuthenticatedUser();
        return notificationRepository.findNotificationsByReceiver(
                authUser,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateUpdated"))
        );
    }

    @Override
    public void markAllSeen() {
        User authUser = userService.getAuthenticatedUser();
        notificationRepository.findNotificationsByReceiverAndIsSeenIsFalse(authUser)
                .forEach(notification -> {
                    if (notification.getReceiver().equals(authUser)) {
                        notification.setIsSeen(true);
                        notification.setDateLastModified(new Date());
                        notificationRepository.save(notification);
                    }
                });
    }

    @Override
    public void markAllRead() {
        User authUser = userService.getAuthenticatedUser();
        notificationRepository.findNotificationsByReceiverAndIsReadIsFalse(authUser)
                .forEach(notification -> {
                    if (notification.getReceiver().equals(authUser)) {
                        notification.setIsSeen(true);
                        notification.setIsRead(true);
                        notification.setDateLastModified(new Date());
                        notificationRepository.save(notification);
                    }
                });
    }

    @Override
    public void deleteNotification(User receiver, Video owningVideo, String type) {
        Notification targetNotification = getNotificationByReceiverAndOwningPostAndType(receiver, owningVideo, type);
        notificationRepository.deleteById(targetNotification.getId());
    }

    @Override
    public void deleteNotificationByOwningPost(Video owningVideo) {
        notificationRepository.deleteNotificationByOwningVideo(owningVideo);
    }

    @Override
    public void deleteNotificationByOwningComment(Commentaire owningCommentaire) {
        notificationRepository.deleteNotificationByOwningCommentaire(owningCommentaire);
    }
}