package com.fiap.gs.demo.shared.cache;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class CacheService {

    private final Map<String, Map<Object, CacheEntry<Object>>> caches = new ConcurrentHashMap<>();

  
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key, long ttlMinutes, Supplier<T> supplier) {
        Map<Object, CacheEntry<Object>> cache = caches.computeIfAbsent(cacheName, k -> new ConcurrentHashMap<>());

        CacheEntry<Object> entry = cache.get(key);

        if (entry == null || entry.isExpired()) {
            T value = supplier.get();
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(ttlMinutes);
            cache.put(key, new CacheEntry<>(value, expirationTime));
            return value;
        }

        return (T) entry.getValue();
    }


  
    public void clearAll() {
        caches.clear();
    }
}

