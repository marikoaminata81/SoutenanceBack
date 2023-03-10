package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Notification;
import com.bezkoder.spring.login.security.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
//@CrossOrigin
@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(@RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<Notification> notifications = notificationService.getNotificationsForAuthUserPaginate(page, size);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping("/notifications/mark-seen")
    public ResponseEntity<?> markAllSeen() {
        notificationService.markAllSeen();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/notifications/mark-read")
    public ResponseEntity<?> markAllRead() {
        notificationService.markAllRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
