package com.example.core_system_rbac.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Hàm này sẽ được gọi khi có người truy cập vào hệ thống
    public void notifyAdmin(String username) {
        String message = "Người dùng " + username + " vừa truy cập vào hệ thống!";

        // Phát thông báo vào kênh "/topic/admin-notifications"
        messagingTemplate.convertAndSend("/topic/admin-notifications", message);
    }
}
