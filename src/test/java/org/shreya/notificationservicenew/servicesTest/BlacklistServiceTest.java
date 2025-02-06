package org.shreya.notificationservicenew.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreya.notificationservicenew.model.Blacklist;
import org.shreya.notificationservicenew.repo.BlacklistRepository;
import org.shreya.notificationservicenew.service.BlacklistService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    @Mock
    private BlacklistRepository blacklistRepository;

    @InjectMocks
    private BlacklistService blacklistService;

    private static final String BLACKLIST_KEY = "notification:blacklist";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Test
    void testIsBlacklisted() {
        String phoneNumber = "9876543210";

        when(setOperations.isMember(BLACKLIST_KEY, phoneNumber)).thenReturn(true);

        boolean result = blacklistService.isBlacklisted(phoneNumber);

        assertTrue(result);
        verify(setOperations).isMember(BLACKLIST_KEY, phoneNumber);
    }

    @Test
    void testAddToBlacklist() {
        List<Blacklist> blacklistEntries = List.of(new Blacklist("9876543210"), new Blacklist("9123456789"));

        blacklistService.addToBlacklist(blacklistEntries);
        verify(blacklistRepository,times(2)).save(any(Blacklist.class));
        verify(setOperations,times(2)).add(eq(BLACKLIST_KEY), any(String.class));
    }

    @Test
    void testRemoveFromBlacklist() {
        List<Blacklist> blacklistEntries = List.of(new Blacklist("9876543210"), new Blacklist("9123456789"));

        blacklistService.removeFromBlacklist(blacklistEntries);
        verify(blacklistRepository,times(2)).delete(any(Blacklist.class));
        verify(setOperations,times(2)).remove(eq(BLACKLIST_KEY), any(String.class));

    }

    @Test
    void testGetAllBlacklistedNumbers() {
        Set<String> mockBlacklist = Set.of("9876543210", "9123456789");

        when(setOperations.members(BLACKLIST_KEY)).thenReturn(mockBlacklist);

        List<String> result = blacklistService.getAllBlacklistedNumbers();

        assertEquals(2, result.size());
        assertTrue(result.contains("9876543210"));
        assertTrue(result.contains("9123456789"));

        verify(setOperations).members(BLACKLIST_KEY);
    }
}
