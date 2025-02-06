package org.shreya.notificationservicenew.controller;

import org.shreya.notificationservicenew.model.NotificationDocument;
import org.shreya.notificationservicenew.repo.ESRepository;
import org.shreya.notificationservicenew.service.ESService;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/sms")
public class ESController {
    @Autowired
    private ESService esService;

    @Autowired
    private ESRepository esRepository;

    @GetMapping("/getLogsByTime")
    public ResponseEntity<?> getSMSlogs(@RequestParam String start, @RequestParam String end) {
        try {
            List<NotificationDocument> logs = esService.getLogsBetweenDates(start, end);
            return ResponseEntity.ok(logs);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(Constants.ERROR, Constants.ERROR_INVALID_DATE_FORMAT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.ERROR_INTERNAL_SERVER, Constants.ERROR_UNEXPECTED_MESSAGE));
        }
    }

    @GetMapping("/getLogsContainingMessage")
    public ResponseEntity<?> getSMSLogsWithMessage(@RequestParam String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(Constants.ERROR, Constants.ERROR_EMPTY_MESSAGE));
            }

            List<NotificationDocument> logs = esService.getLogsContainingMessage(message);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.ERROR_INTERNAL_SERVER, Constants.ERROR_UNEXPECTED_MESSAGE));
        }
    }
}