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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Trilha Service Tests")
class TrilhaServiceTest {

    @Mock
    private TrilhaRepository trilhaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicoRepository topicoRepository;

    @Mock
    private TrilhaGeneratorService trilhaGeneratorService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private TrilhaService trilhaService;

    private CreateTrilhaDTO validTrilhaDTO;
    private User user;
    private Trilha trilha;
    private Topico topico;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@example.com")
                .build();

        topico = Topico.builder()
                .id(1L)
                .nome("Java Básico")
                .nivel(NivelTopicoEnum.BASICO)
                .tema(TemasEnum.BACKEND)
                .build();

        validTrilhaDTO = new CreateTrilhaDTO();
        validTrilhaDTO.setNome("Trilha de Java");
        validTrilhaDTO.setIdUser(1L);

        trilha = Trilha.builder()
                .id(1L)
                .nome("Trilha de Java")
                .user(user)
                .status(StatusTrilhaEnum.CRIADA)
                .topicos(new HashSet<>())
                .trilhasRelacionadas(new HashSet<>())
                .build();
    }

    @Test
    @DisplayName("Deve criar trilha sem tópicos")
    void shouldCreateTrilhaSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trilhaRepository.save(any(Trilha.class))).thenReturn(trilha);

        TrilhaResponseDTO result = trilhaService.createTrilha(validTrilhaDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(validTrilhaDTO.getNome());
        assertThat(result.getStatus()).isEqualTo(StatusTrilhaEnum.CRIADA);
        
        verify(userRepository).findById(1L);
        verify(trilhaRepository).save(any(Trilha.class));
    }

    @Test
    @DisplayName("Deve criar trilha com tópicos")
    void shouldCreateTrilhaWithTopicos() {
        Set<Long> topicoIds = new HashSet<>(Arrays.asList(1L));
        validTrilhaDTO.setIdTopicos(topicoIds);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));
        when(trilhaRepository.save(any(Trilha.class))).thenReturn(trilha);

        TrilhaResponseDTO result = trilhaService.createTrilha(validTrilhaDTO);

        
        assertThat(result).isNotNull();
        verify(topicoRepository).findById(1L);
        verify(trilhaRepository).save(any(Trilha.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trilhaService.createTrilha(validTrilhaDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        verify(userRepository).findById(1L);
        verify(trilhaRepository, never()).save(any(Trilha.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tópico não encontrado")
    void shouldThrowExceptionWhenTopicoNotFound() {
        Set<Long> topicoIds = new HashSet<>(Arrays.asList(999L));
        validTrilhaDTO.setIdTopicos(topicoIds);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(topicoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trilhaService.createTrilha(validTrilhaDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tópico não encontrado");

        verify(topicoRepository).findById(999L);
        verify(trilhaRepository, never()).save(any(Trilha.class));
    }

    @Test
    @DisplayName("Deve criar trilha com trilhas relacionadas")
    void shouldCreateTrilhaWithRelatedTrilhas() {
        Trilha relatedTrilha = Trilha.builder()
                .id(2L)
                .nome("Trilha Relacionada")
                .build();

        Set<Long> relatedIds = new HashSet<>(Arrays.asList(2L));
        validTrilhaDTO.setIdTrilhasRelacionadas(relatedIds);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trilhaRepository.findById(2L)).thenReturn(Optional.of(relatedTrilha));
        when(trilhaRepository.save(any(Trilha.class))).thenReturn(trilha);

        TrilhaResponseDTO result = trilhaService.createTrilha(validTrilhaDTO);

        assertThat(result).isNotNull();
        verify(trilhaRepository).findById(2L);
        verify(trilhaRepository).save(any(Trilha.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando trilha relacionada não encontrada")
    void shouldThrowExceptionWhenRelatedTrilhaNotFound() {
        Set<Long> relatedIds = new HashSet<>(Arrays.asList(999L));
        validTrilhaDTO.setIdTrilhasRelacionadas(relatedIds);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trilhaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trilhaService.createTrilha(validTrilhaDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Trilha relacionada não encontrada");

        verify(trilhaRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar trilha por ID usando cache")
    void shouldGetTrilhaByIdWithCache() {
        TrilhaResponseDTO expectedResponse = TrilhaResponseDTO.toDTO(trilha);
        
        when(cacheService.get(anyString(), anyLong(), anyLong(), any(Supplier.class)))
                .thenReturn(expectedResponse);

        TrilhaResponseDTO result = trilhaService.getTrilhaById(1L);

        assertThat(result).isNotNull();
        verify(cacheService).get(eq("TRILHA"), eq(1L), eq(10L), any(Supplier.class));
    }

    @Test
    @DisplayName("Deve retornar lista de sample trilhas")
    void shouldGetSampleTrilhas() {
        List<Trilha> trilhas = Arrays.asList(
                Trilha.builder().id(1L).nome("Trilha 1").build(),
                Trilha.builder().id(2L).nome("Trilha 2").build()
        );
        
        when(trilhaRepository.findAll()).thenReturn(trilhas);

        List<SampleTrilhaDTO> result = trilhaService.getSampleTrilhas();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNome()).isEqualTo("Trilha 1");
        
        verify(trilhaRepository).findAll();
    }

    @Test
    @DisplayName("Deve gerar trilha aleatória")
    void shouldGenerateRandomTrilha() {
        when(trilhaGeneratorService.generateRandomTrilha(1L, TemasEnum.BACKEND, NivelTopicoEnum.BASICO))
                .thenReturn(trilha);

        TrilhaResponseDTO result = trilhaService.generateRandomTrilha(1L, TemasEnum.BACKEND, NivelTopicoEnum.BASICO);

        assertThat(result).isNotNull();
        verify(trilhaGeneratorService).generateRandomTrilha(1L, TemasEnum.BACKEND, NivelTopicoEnum.BASICO);
    }

    @Test
    @DisplayName("Deve finalizar trilha com sucesso")
    void shouldFinalizeTrilhaSuccessfully() {
        when(trilhaRepository.findById(1L)).thenReturn(Optional.of(trilha));

        TrilhaResponseDTO result = trilhaService.finalizarTrilha(1L);

        assertThat(result).isNotNull();
        verify(trilhaRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao finalizar trilha inexistente")
    void shouldThrowExceptionWhenFinalizingNonExistentTrilha() {
        when(trilhaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trilhaService.finalizarTrilha(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Trilha não encontrada");

        verify(trilhaRepository).findById(999L);
    }
}

