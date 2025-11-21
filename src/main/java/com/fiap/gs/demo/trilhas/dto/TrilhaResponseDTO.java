package com.fiap.gs.demo.trilhas.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fiap.gs.demo.trilhas.Trilha;
import com.fiap.gs.demo.trilhas.enums.StatusTrilhaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TrilhaResponseDTO {

    private Long id;
    private String nome;
    private Long idUser;
    private String nomeUser;
    private StatusTrilhaEnum status;
    private Set<InnerTopicosTrilhaDTO> topicos;
    private Set<SampleTrilhaDTO> trilhasRelacionadas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InnerTopicosTrilhaDTO {
        private Long id;
        private String nome;
    }

    public static TrilhaResponseDTO toDTO(Trilha trilha) {
        Set<InnerTopicosTrilhaDTO> topicosDTO = trilha.getTopicos() != null ? 
            trilha.getTopicos().stream()
                .map(t -> InnerTopicosTrilhaDTO.builder()
                    .id(t.getId())
                    .nome(t.getNome())
                    .build())
                .collect(Collectors.toSet()) : null;

        Set<SampleTrilhaDTO> trilhasRelacionadasDTO = trilha.getTrilhasRelacionadas() != null ?
            trilha.getTrilhasRelacionadas().stream()
                .map(t -> SampleTrilhaDTO.builder()
                    .id(t.getId())
                    .nome(t.getNome())
                    .build())
                .collect(Collectors.toSet()) : null;

        return TrilhaResponseDTO.builder()
                .id(trilha.getId())
                .nome(trilha.getNome())
                .idUser(trilha.getUser() != null ? trilha.getUser().getId() : null)
                .nomeUser(trilha.getUser() != null ? trilha.getUser().getNome() : null)
                .status(trilha.getStatus())
                .topicos(topicosDTO)
                .trilhasRelacionadas(trilhasRelacionadasDTO)
                .build();
    }

}

