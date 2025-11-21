package com.fiap.gs.demo.trilhas;

import com.fiap.gs.demo.exceptions.NotFoundException;
import com.fiap.gs.demo.exceptions.user.UserNotFoundException;
import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.shared.cache.CacheService;
import com.fiap.gs.demo.topicos.Topico;
import com.fiap.gs.demo.topicos.TopicoRepository;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
import com.fiap.gs.demo.trilhas.dto.CreateTrilhaDTO;
import com.fiap.gs.demo.trilhas.dto.SampleTrilhaDTO;
import com.fiap.gs.demo.trilhas.dto.TrilhaResponseDTO;
import com.fiap.gs.demo.trilhas.enums.StatusTrilhaEnum;
import com.fiap.gs.demo.users.User;
import com.fiap.gs.demo.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrilhaService {

    private static final String CACHE_NAME = "TRILHA";
    private static final long CACHE_TTL_MINUTES = 10; 

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private TrilhaGeneratorService trilhaGeneratorService;

    @Autowired
    private CacheService cacheService;

    @Transactional
    public TrilhaResponseDTO createTrilha(CreateTrilhaDTO createTrilhaDTO) {
        User user = userRepository.findById(createTrilhaDTO.getIdUser())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Trilha trilha = Trilha.builder()
                .nome(createTrilhaDTO.getNome())
                .user(user)
                .status(StatusTrilhaEnum.CRIADA)
                .topicos(new HashSet<>())
                .trilhasRelacionadas(new HashSet<>())
                .build();

        if (createTrilhaDTO.getIdTopicos() != null && !createTrilhaDTO.getIdTopicos().isEmpty()) {
            Set<Topico> topicos = createTrilhaDTO.getIdTopicos().stream()
                    .map(idTopico -> topicoRepository.findById(idTopico)
                            .orElseThrow(() -> new NotFoundException("Tópico não encontrado: " + idTopico)))
                    .collect(Collectors.toSet());
            
            trilha.setTopicos(topicos);
        }

        if (createTrilhaDTO.getIdTrilhasRelacionadas() != null && 
            !createTrilhaDTO.getIdTrilhasRelacionadas().isEmpty()) {
            
            Set<Trilha> trilhasRelacionadas = createTrilhaDTO.getIdTrilhasRelacionadas().stream()
                    .map(idTrilhaRelacionada -> trilhaRepository.findById(idTrilhaRelacionada)
                            .orElseThrow(() -> new NotFoundException("Trilha relacionada não encontrada: " + idTrilhaRelacionada)))
                    .collect(Collectors.toSet());
            
            trilha.setTrilhasRelacionadas(trilhasRelacionadas);
        }

        Trilha savedTrilha = trilhaRepository.save(trilha);
        return TrilhaResponseDTO.toDTO(savedTrilha);
    }

    public TrilhaResponseDTO getTrilhaById(Long id) {
        return cacheService.get(
            CACHE_NAME, 
            id, 
            CACHE_TTL_MINUTES, 
            () -> {
                Trilha trilha = trilhaRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Trilha não encontrada"));
                return TrilhaResponseDTO.toDTO(trilha);
            }
        );
    }

    public List<SampleTrilhaDTO> getSampleTrilhas() {
        return trilhaRepository.findAll().stream()
                .map(trilha -> SampleTrilhaDTO.builder()
                    .id(trilha.getId())
                    .nome(trilha.getNome())
                    .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public TrilhaResponseDTO generateRandomTrilha(Long userId, TemasEnum tema, NivelTopicoEnum nivel) {
        Trilha trilhaGerada = trilhaGeneratorService.generateRandomTrilha(userId, tema, nivel);
        return TrilhaResponseDTO.toDTO(trilhaGerada);
    }

    @Transactional
    public TrilhaResponseDTO finalizarTrilha(Long id) {
        Trilha trilha = trilhaRepository.findById(id).orElseThrow(() -> new NotFoundException("Trilha não encontrada"));
        trilha.finalizarTrilha();
        return TrilhaResponseDTO.toDTO(trilha);
    }
}

