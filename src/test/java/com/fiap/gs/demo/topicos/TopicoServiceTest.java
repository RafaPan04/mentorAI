package com.fiap.gs.demo.topicos;

import com.fiap.gs.demo.exceptions.NotFoundException;
import com.fiap.gs.demo.exceptions.topico.TopicoNotFoundException;
import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.dto.CreateTopicoDTO;
import com.fiap.gs.demo.topicos.dto.TopicoResponseDTO;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Topico Service Tests")
class TopicoServiceTest {

    @Mock
    private TopicoRepository topicoRepository;

    @InjectMocks
    private TopicoService topicoService;

    private CreateTopicoDTO validTopicoDTO;
    private Topico topico;

    @BeforeEach
    void setUp() {
        validTopicoDTO = new CreateTopicoDTO();
        validTopicoDTO.setNome("Introdução ao Java");
        validTopicoDTO.setNivel(NivelTopicoEnum.BASICO);
        validTopicoDTO.setTema(TemasEnum.BACKEND);
        validTopicoDTO.setConteudo("Conteúdo sobre Java básico");
        validTopicoDTO.setReferencias("https://docs.oracle.com/javase");

        topico = Topico.builder()
                .id(1L)
                .nome("Introdução ao Java")
                .nivel(NivelTopicoEnum.BASICO)
                .tema(TemasEnum.BACKEND)
                .conteudo("Conteúdo sobre Java básico")
                .referencias("https://docs.oracle.com/javase")
                .curtidas(0)
                .build();
    }

    @Test
    @DisplayName("Deve criar tópico sem tópicos relacionados")
    void shouldCreateTopicoSuccessfully() {
        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);

        TopicoResponseDTO result = topicoService.createTopico(validTopicoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(validTopicoDTO.getNome());
        assertThat(result.getNivel()).isEqualTo(validTopicoDTO.getNivel());
        assertThat(result.getTema()).isEqualTo(validTopicoDTO.getTema());
        
        verify(topicoRepository).save(any(Topico.class));
    }

    @Test
    @DisplayName("Deve criar tópico com tópicos relacionados")
    void shouldCreateTopicoWithRelatedTopics() {
        Topico relatedTopico = Topico.builder()
                .id(2L)
                .nome("Java Avançado")
                .build();
        
        Set<Long> relatedIds = new HashSet<>();
        relatedIds.add(2L);
        validTopicoDTO.setIdTopicosRelacionados(relatedIds);

        when(topicoRepository.findById(2L)).thenReturn(Optional.of(relatedTopico));
        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);

        TopicoResponseDTO result = topicoService.createTopico(validTopicoDTO);

        assertThat(result).isNotNull();
        var argumentCaptor = ArgumentCaptor.forClass(Topico.class);
        verify(topicoRepository).save(argumentCaptor.capture());
        var capturedTopico = argumentCaptor.getValue();
        assertThat(capturedTopico.getTopicosRelacionados()).isNotNull();
        assertThat(capturedTopico.getTopicosRelacionados().size()).isEqualTo(1);
        assertThat(capturedTopico.getNome()).isEqualTo(topico.getNome());
        assertThat(capturedTopico.getTema()).isEqualTo(topico.getTema());
        assertThat(capturedTopico.getNivel()).isEqualTo(topico.getNivel());
        
    }

    @Test
    @DisplayName("Deve lançar exceção quando tópico relacionado não existe")
    void shouldThrowExceptionWhenRelatedTopicoNotFound() {
        Set<Long> relatedIds = new HashSet<>();
        relatedIds.add(999L);
        validTopicoDTO.setIdTopicosRelacionados(relatedIds);

        when(topicoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topicoService.createTopico(validTopicoDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tópico relacionado não encontrado");

        verify(topicoRepository).findById(999L);
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    @DisplayName("Deve buscar tópico por ID com sucesso")
    void shouldGetTopicoByIdSuccessfully() {
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));

        TopicoResponseDTO result = topicoService.getTopicoById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo(topico.getNome());
        
        verify(topicoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tópico não encontrado")
    void shouldThrowExceptionWhenTopicoNotFound() {
        when(topicoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topicoService.getTopicoById(999L))
                .isInstanceOf(TopicoNotFoundException.class)
                .hasMessageContaining("Tópico não encontrado");

        verify(topicoRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve curtir tópico com sucesso")
    void shouldLikeTopicoSuccessfully() {
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));
        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);

        TopicoResponseDTO result = topicoService.curtirTopico(1L);

        assertThat(result).isNotNull();
        verify(topicoRepository).findById(1L);
        verify(topicoRepository).save(topico);
    }

    @Test
    @DisplayName("Deve lançar exceção ao curtir tópico inexistente")
    void shouldThrowExceptionWhenLikingNonExistentTopico() {
        when(topicoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> topicoService.curtirTopico(999L))
                .isInstanceOf(TopicoNotFoundException.class)
                .hasMessageContaining("Tópico não encontrado");

        verify(topicoRepository).findById(999L);
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    @DisplayName("Deve incrementar curtidas ao curtir tópico")
    void shouldIncrementLikesWhenLiking() {
        Topico topicoComCurtidas = Topico.builder()
                .id(1L)
                .nome("Test")
                .curtidas(5)
                .build();
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topicoComCurtidas));
        when(topicoRepository.save(any(Topico.class))).thenAnswer(invocation -> {
            Topico saved = invocation.getArgument(0);
            assertThat(saved.getCurtidas()).isEqualTo(6);
            return saved;
        });

        topicoService.curtirTopico(1L);

        verify(topicoRepository).save(any(Topico.class));
    }
}

