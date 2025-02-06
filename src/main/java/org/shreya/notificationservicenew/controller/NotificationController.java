package org.shreya.notificationservicenew.controller;

import jakarta.validation.Valid;
import org.shreya.notificationservicenew.model.Notification;
import org.shreya.notificationservicenew.model.NotificationRequest;
import org.shreya.notificationservicenew.service.NotificationService;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/sms")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@Valid @RequestBody NotificationRequest request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(Constants.ERROR, errors));
            }

            Notification notification = notificationService.createNotification(request);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(Constants.ERROR, e.getMessage()));
        }

    }

    @GetMapping("/{request_id}")
    public ResponseEntity<?> getNotificationDetails(@PathVariable("request_id") String request_id){
        try {
            Notification details = notificationService.getNotificationById(request_id);
            if (details == null) throw new NoSuchElementException();
            return ResponseEntity.ok(details);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            Constants.ERROR, Map.of(
                                    "code", Constants.ERROR_INVALID_REQUEST,
                                    "message", Constants.ERROR_REQUEST_ID_NOT_FOUND_MESSAGE
                            )
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            Constants.ERROR, Map.of(
                                    "code", Constants.ERROR_INVALID_REQUEST,
                                    "message", Constants.ERROR_REQUEST_ID_NOT_FOUND_MESSAGE
                            )
                    ));
        }
    }
}
