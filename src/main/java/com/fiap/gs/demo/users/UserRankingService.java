package com.fiap.gs.demo.users;

import com.fiap.gs.demo.trilhas.Trilha;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Service
@Slf4j
public class UserRankingService {

    @Autowired
    private UserRepository userRepository;

    private PriorityQueue<UserRankingDTO> rankingQueue;

    @Transactional
    public void initializeRanking() {
        rankingQueue = new PriorityQueue<>();
        loadAllUsersIntoRanking();
    }
    @Transactional
    public void loadAllUsersIntoRanking() {
        rankingQueue.clear(); 
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getTrilhas() == null) {
                continue;
            }
            long trilhasFinalizadas = user.getTrilhas().stream()
                    .filter(Trilha::isFinalizada)
                    .count();

            UserRankingDTO rankingDTO = UserRankingDTO.builder()
                    .id(user.getId())
                    .nome(user.getNome())
                    .apelido(user.getApelido())
                    .trilhasFinalizadas(trilhasFinalizadas)
                    .quantidadeTrilhas(Long.valueOf(user.getTrilhas().size()))
                    .build();

            rankingQueue.offer(rankingDTO);
        }
    }

    public List<UserRankingDTO> getRanking() {
        return new ArrayList<>(rankingQueue);
    }


}

