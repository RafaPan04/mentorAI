package com.fiap.gs.demo.topicos.dto;

import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.Topico;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicoResponseDTO {

    private Long id;
    private String nome;
    private NivelTopicoEnum nivel;
    private TemasEnum tema;
    private Integer curtidas;
    private String conteudo;
    private String referencias;
    private Set<TopicoSimpleDTO> topicosRelacionados;


    public static TopicoResponseDTO toDTO(Topico topico) {
        Set<TopicoSimpleDTO> relacionados = null;
        if (topico.getTopicosRelacionados() != null) {
            relacionados = topico.getTopicosRelacionados().stream()
                    .map(t -> new TopicoSimpleDTO(t.getId(), t.getNome(), t.getTema(), t.getNivel()))
                    .collect(Collectors.toSet());
        }

        return TopicoResponseDTO.builder()
                .id(topico.getId())
                .nome(topico.getNome())
                .nivel(topico.getNivel())
                .tema(topico.getTema())
                .curtidas(topico.getCurtidas())
                .conteudo(topico.getConteudo())
                .referencias(topico.getReferencias())
                .topicosRelacionados(relacionados)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicoSimpleDTO {
        private Long id;
        private String nome;
        private TemasEnum tema;
        private NivelTopicoEnum nivel;
    }
}

