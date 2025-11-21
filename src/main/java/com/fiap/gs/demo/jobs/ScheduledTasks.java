package com.fiap.gs.demo.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import com.fiap.gs.demo.shared.cache.CacheService;
import com.fiap.gs.demo.users.UserRankingService;

@Component
public class ScheduledTasks {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserRankingService userRankingService;

   
    @Scheduled(fixedRate = 1800000)
    public void cleanExpiredCaches() {
        cacheService.clearAll();
    }

    @Scheduled(fixedRate = 1800000)
    public void initializeRanking() {
        userRankingService.initializeRanking();
    }
    
}
