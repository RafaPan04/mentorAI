package com.fiap.gs.demo.trilhas;

import com.fiap.gs.demo.exceptions.user.UserNotFoundException;
import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.Topico;
import com.fiap.gs.demo.topicos.TopicoRepository;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
import com.fiap.gs.demo.trilhas.enums.StatusTrilhaEnum;
import com.fiap.gs.demo.users.User;
import com.fiap.gs.demo.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrilhaGeneratorService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int MIN_TOPICOS = 3;
    private static final int MAX_TOPICOS = 8;
    private static final Random random = new Random();

    public Trilha generateRandomTrilha(Long userId, TemasEnum tema, NivelTopicoEnum nivel) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        List<Topico> topicosDisponiveis = topicoRepository.findByTemaAndNivel(tema, nivel);

        if (topicosDisponiveis.isEmpty()) {
            throw new RuntimeException("Não foi possível encontrar tópicos para o tema " + tema + " e nível " + nivel);
        }

        Set<Topico> topicosEscolhidos = selectConnectedTopicos(topicosDisponiveis);

        if (topicosEscolhidos.isEmpty()) {
            throw new RuntimeException("Não foi possível selecionar tópicos para a trilha");
        }

        Trilha trilha = new Trilha();
        trilha.setNome(generateTrilhaName(tema, nivel));
        trilha.setUser(user);
        trilha.setStatus(StatusTrilhaEnum.CRIADA);
        trilha.setTopicos(topicosEscolhidos);
        trilha.setTrilhasRelacionadas(new HashSet<>());

        return trilhaRepository.save(trilha);
    }

    /**
     * Seleciona tópicos conectados usando os relacionamentos
     * Algoritmo: começa com um tópico aleatório e expande através dos relacionamentos
     */
    private Set<Topico> selectConnectedTopicos(List<Topico> topicosDisponiveis) {
        if (topicosDisponiveis.isEmpty()) {
            return new HashSet<>();
        }

        Set<Topico> topicosEscolhidos = new LinkedHashSet<>();
        Set<Long> topicosVisitados = new HashSet<>();
        Queue<Topico> filaPrioridade = new LinkedList<>();

        int targetSize = MIN_TOPICOS + random.nextInt(MAX_TOPICOS - MIN_TOPICOS + 1);
        targetSize = Math.min(targetSize, topicosDisponiveis.size());

        Topico topicoInicial = findBestStartingTopico(topicosDisponiveis);
        
        filaPrioridade.add(topicoInicial);
        topicosVisitados.add(topicoInicial.getId());

        while (!filaPrioridade.isEmpty() && topicosEscolhidos.size() < targetSize) {
            Topico topicoAtual = filaPrioridade.poll();
            topicosEscolhidos.add(topicoAtual);

            Optional<Topico> topicoComRelacionados = topicoRepository.findByIdWithRelacionados(topicoAtual.getId());
            
            if (topicoComRelacionados.isPresent() && topicoComRelacionados.get().getTopicosRelacionados() != null) {
                Set<Topico> relacionados = topicoComRelacionados.get().getTopicosRelacionados();
                
                List<Topico> relacionadosValidos = relacionados.stream()
                        .filter(t -> topicosDisponiveis.stream()
                                .anyMatch(td -> td.getId().equals(t.getId())))
                        .filter(t -> !topicosVisitados.contains(t.getId()))
                        .collect(Collectors.toList());

                Collections.shuffle(relacionadosValidos);
                int toAdd = Math.min(relacionadosValidos.size(), 3);
                
                for (int i = 0; i < toAdd; i++) {
                    Topico relacionado = relacionadosValidos.get(i);
                    if (!topicosVisitados.contains(relacionado.getId())) {
                        filaPrioridade.add(relacionado);
                        topicosVisitados.add(relacionado.getId());
                    }
                }
            }

            if (filaPrioridade.isEmpty() && topicosEscolhidos.size() < targetSize) {
                List<Topico> naoVisitados = topicosDisponiveis.stream()
                        .filter(t -> !topicosVisitados.contains(t.getId()))
                        .collect(Collectors.toList());
                
                if (!naoVisitados.isEmpty()) {
                    Topico randomTopico = naoVisitados.get(random.nextInt(naoVisitados.size()));
                    filaPrioridade.add(randomTopico);
                    topicosVisitados.add(randomTopico.getId());
                }
            }
        }

        return topicosEscolhidos;
    }

    private Topico findBestStartingTopico(List<Topico> topicosDisponiveis) {
        List<Topico> topicosComRelacionamentos = topicosDisponiveis.stream()
                .map(t -> topicoRepository.findByIdWithRelacionados(t.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(t -> t.getTopicosRelacionados() != null && !t.getTopicosRelacionados().isEmpty())
                .collect(Collectors.toList());

        if (!topicosComRelacionamentos.isEmpty()) {
            topicosComRelacionamentos.sort((a, b) -> 
                Integer.compare(b.getCurtidas(), a.getCurtidas()));
            
            int index = random.nextInt(Math.min(3, topicosComRelacionamentos.size()));
            return topicosComRelacionamentos.get(index);
        }

        return topicosDisponiveis.get(random.nextInt(topicosDisponiveis.size()));
    }

    private String generateTrilhaName(TemasEnum tema, NivelTopicoEnum nivel) {
        String[] prefixos = {"Trilha", "Jornada", "Caminho", "Curso", "Aprendizado"};
        String prefixo = prefixos[random.nextInt(prefixos.length)];
        
        return String.format("%s de %s - %s", 
                prefixo, 
                tema.getLabel(), 
                nivel.getLabel());
    }
}

