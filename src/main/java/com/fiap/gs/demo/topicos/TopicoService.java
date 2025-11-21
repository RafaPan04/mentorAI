package com.fiap.gs.demo.topicos;

import com.fiap.gs.demo.exceptions.BadRequestException;
import com.fiap.gs.demo.exceptions.NotFoundException;
import com.fiap.gs.demo.exceptions.topico.TopicoNotFoundException;
import com.fiap.gs.demo.topicos.dto.CreateTopicoDTO;
import com.fiap.gs.demo.topicos.dto.TopicoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Transactional
    public TopicoResponseDTO createTopico(CreateTopicoDTO createTopicoDTO) {

        Topico topico = Topico.builder()
                .nome(createTopicoDTO.getNome())
                .nivel(createTopicoDTO.getNivel())
                .tema(createTopicoDTO.getTema())
                .conteudo(createTopicoDTO.getConteudo())
                .referencias(createTopicoDTO.getReferencias())
                .build();

        if (createTopicoDTO.getIdTopicosRelacionados() != null && 
            !createTopicoDTO.getIdTopicosRelacionados().isEmpty()) {
            
            Set<Topico> relacionados = createTopicoDTO.getIdTopicosRelacionados().stream()
                    .map(id -> topicoRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Tópico relacionado não encontrado: " + id)))
                    .collect(Collectors.toSet());
            
            topico.setTopicosRelacionados(relacionados);
        }

        Topico savedTopico = topicoRepository.save(topico);
        return TopicoResponseDTO.toDTO(savedTopico);
    }

    public TopicoResponseDTO getTopicoById(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNotFoundException("Tópico não encontrado: " + id));
        
        return TopicoResponseDTO.toDTO(topico);
    }

    @Transactional
    public TopicoResponseDTO curtirTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNotFoundException("Tópico não encontrado: " + id));
        
        topico.adicionarCurtida();
        Topico savedTopico = topicoRepository.save(topico);
        
        return TopicoResponseDTO.toDTO(savedTopico);
    }
}

