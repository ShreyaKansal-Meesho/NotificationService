package org.shreya.notificationservicenew.service;

import org.shreya.notificationservicenew.model.Enum.Status;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.repo.NotificationRepository;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaService {
    private final NotificationService notificationService;
    private final BlacklistService blacklistService;
    private final NotificationRepository notificationRepository;
    private final ESService esService;

    public KafkaService(NotificationService notificationService, BlacklistService blacklistService, NotificationRepository notificationRepository, ESService esService) {
        this.notificationService = notificationService;
        this.blacklistService = blacklistService;
        this.notificationRepository = notificationRepository;
        this.esService = esService;
    }

    public void processNotification(String requestId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(requestId);

        if (notificationOpt.isEmpty()) return;
        Notification notification = notificationOpt.get();

        if (blacklistService.isBlacklisted(notification.getPhoneNumber())) {
            notificationService.updateNotification(requestId, Status.failed, Constants.ERROR_BLACKLISTED, Constants.ERROR_BLACKLISTED_MESSAGE);
        } else {
            boolean success = sendSmsViaThirdPartyApi(notification);

            if (success) {
                notificationService.updateNotification(requestId, Status.sent, null, null);
            } else {
                notificationService.updateNotification(requestId, Status.failed, Constants.ERROR_API, Constants.ERROR_API_MESSAGE);
            }
        }

        esService.saveLog(notification);
    }

    private boolean sendSmsViaThirdPartyApi(Notification notification) {
        //how to implement??
        return true;
    }
}
