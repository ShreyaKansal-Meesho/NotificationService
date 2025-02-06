package org.shreya.notificationservicenew.service;

import org.shreya.notificationservicenew.model.Blacklist;
import org.shreya.notificationservicenew.repo.BlacklistRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.shreya.notificationservicenew.util.Constants.BLACKLIST_KEY;

@Service
public class BlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    private final BlacklistRepository blacklistRepository;

    public BlacklistService(RedisTemplate<String, String> redisTemplate, BlacklistRepository blacklistRepository) {
        this.redisTemplate = redisTemplate;
        this.blacklistRepository = blacklistRepository;
    }

    public boolean isBlacklisted(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(BLACKLIST_KEY, phoneNumber));
    }

    public void addToBlacklist(List<Blacklist> phoneNumber) {
        for (Blacklist item : phoneNumber) {
            blacklistRepository.save(new Blacklist(item.getPhoneNumber()));
            redisTemplate.opsForSet().add(BLACKLIST_KEY, item.getPhoneNumber());
        }
    }

    public void removeFromBlacklist(List<Blacklist> phoneNumber) {
        for (Blacklist item : phoneNumber) {
            blacklistRepository.delete(new Blacklist(item.getPhoneNumber()));
            redisTemplate.opsForSet().remove(BLACKLIST_KEY, item.getPhoneNumber());
        }
    }

    public List<String> getAllBlacklistedNumbers() {
        Set<String> blacklistedNumbers = redisTemplate.opsForSet().members(BLACKLIST_KEY);
        return blacklistedNumbers != null ? new ArrayList<>(blacklistedNumbers) : List.of();
    }
}
