package org.shreya.notificationservicenew.controller;

import org.shreya.notificationservicenew.model.Blacklist;
import org.shreya.notificationservicenew.service.BlacklistService;
import org.shreya.notificationservicenew.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/blacklist")
public class BlacklistController {
    private final BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    public ResponseEntity<?> getBlacklist() {
        try {
            List<String> blacklistedNumbers = blacklistService.getAllBlacklistedNumbers();
            return ResponseEntity.ok(Map.of("data", blacklistedNumbers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.ERROR, Constants.ERROR_UNEXPECTED_MESSAGE));
        }
    }

    @PostMapping
    public ResponseEntity<?> addBlacklist(@RequestBody List<Blacklist> blacklist) {
        try {
            blacklistService.addToBlacklist(blacklist);
            return ResponseEntity.ok(Map.of("data", "Successfully blacklisted"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.ERROR_INTERNAL_SERVER, Constants.ERROR_UNEXPECTED_MESSAGE));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeBlacklist(@RequestBody List<Blacklist> blacklist) {
        try {
            blacklistService.removeFromBlacklist(blacklist);
            return ResponseEntity.ok(Map.of("data", "Successfully whitelisted"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.ERROR_INTERNAL_SERVER, Constants.ERROR_UNEXPECTED_MESSAGE));
        }
    }
}
