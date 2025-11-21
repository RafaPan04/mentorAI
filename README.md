# 📚 MentorAI - FIAP


## Autores
- Guilherme Oliveira Da Silva -  558797
- Rafael Panhoca - 555014
- Marco Antonio Andrade Goncalves - 556818


# Atenção*****

- Esse projeto inclui funcionalidadades para as entrefas de Domain Driven Design e Dynamic Programming. **Ao fim do documento está as explicaçõs para Dynamic Programming**


## 🛠️ Tecnologias Utilizadas

### Versões
- **Java**: 21
- **Spring Boot**: 3.3.5
- **Maven**: 3.6+

### Dependências Principais

| Dependência | Versão | Descrição |
|------------|--------|-----------|
| spring-boot-starter-web | 3.3.5 | Framework web para criar APIs REST |
| spring-boot-starter-data-jpa | 3.3.5 | Persistência de dados com JPA/Hibernate |
| spring-boot-starter-validation | 3.3.5 | Validação de beans com Jakarta Validation |
| h2 | - | Banco de dados em memória |
| liquibase-core | - | Controle de versão do banco de dados |
| lombok | - | Redução de código boilerplate |
| springdoc-openapi-starter-webmvc-ui | 2.6.0 | Documentação OpenAPI/Swagger |
| spring-boot-devtools | 3.3.5 | Ferramentas de desenvolvimento |
| spring-boot-starter-test | 3.3.5 | Testes unitários e de integração |

## 📁 Estrutura do Projeto

```
src/main/java/com/fiap/gs/demo/
├── DemoApplication.java                    # Classe principal da aplicação
├── exceptions/                             # Tratamento de exceções customizadas
├── jobs/
│   └── ScheduledTasks.java                # Tarefas agendadas (limpeza de cache)
├── shared/                                 # Recursos compartilhados
│   ├── cache/                             # Sistema de cache customizado
├── users/                                  # Módulo de usuáriosUserRankingDTO
├── topicos/                                # Módulo de tópicos de aprendizado
└── trilhas/                                # Módulo de trilhas de aprendizado

src/test/java/com/fiap/gs/demo/
├── DemoApplicationTests.java              # Teste de contexto Spring
├── topicos/
├── trilhas/


src/main/resources/
├── application.properties                  # Configurações da aplicação
└── db/changelog/                          # Controle de versão do banco (Liquibase)
    ├── db.changelog-master.xml
    └── changes/                           # Migrations SQL
        ├── 002-create-users-table.xml
        ├── 003-create-topicos-table.xml
        ├── 004-create-trilhas-table.xml
        └── 005-insert-mock-data.xml
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21 instalado
- Maven 3.6+ (ou usar o Maven Wrapper incluído)

### Executando a aplicação

```bash
# Via Maven Wrapper
./mvnw spring-boot:run

# Ou via Maven instalado
mvn spring-boot:run

```

A aplicação estará disponível em: **`http://localhost:8080`**

## 🗄️ Banco de Dados H2

### Console H2
O console web do H2 está habilitado e pode ser acessado em:
```
http://localhost:8080/h2-console
```

**Credenciais de acesso:**
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: _(deixe em branco)_


## 📖 Documentação da API (Swagger)

A documentação interativa da API está disponível através do Swagger UI:

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```


##  Exemplo de endpoints

**Todos os endpoints da aplicação podem ser consultados no swagger**

### 👤 Users - Gerenciamento de Usuários

#### Criar Usuário
```bash
POST http://localhost:8080/users
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "senha": "senha123",
  "pontuacao": 0
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "pontuacao": 0
}
```


---

### 📝 Topicos - Gerenciamento de Tópicos

#### Criar Tópico
```bash
POST http://localhost:8080/topicos
Content-Type: application/json

{
  "titulo": "Introdução ao Spring Boot",
  "descricao": "Aprenda os fundamentos do Spring Boot",
  "tema": "TECNOLOGIA",
  "nivel": "BASICO",
  "conteudo": "Spring Boot é um framework que facilita...",
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "titulo": "Introdução ao Spring Boot",
  "descricao": "Aprenda os fundamentos do Spring Boot",
  "tema": "TECNOLOGIA",
  "nivel": "BASICO",
  "conteudo": "Spring Boot é um framework que facilita...",
  "curtidas": 0,
  "prerequisitos": []
}
```
---

### 🎯 Trilhas - Gerenciamento de Trilhas de Aprendizado

#### Gerar Trilha Aleatória
```bash
POST http://localhost:8080/trilhas/random-generate?userId={userId}&tema={tema}&nivel={nivel}
```

**Resposta (201 Created):**
```json
{
  "id": 3,
  "titulo": "Trilha Gerada: TECNOLOGIA - BASICO",
  "descricao": "Trilha gerada automaticamente",
  "userId": 1,
  "topicos": [
    {
      "id": 1,
      "titulo": "Tópico 1"
    },
    {
      "id": 4,
      "titulo": "Tópico 2"
    }
  ],
  "status": "NAO_INICIADA",
  "dataCriacao": "2025-11-14T10:45:00"
}
```

---

```

## ⚙️ Configurações (application.properties)

### Banco de Dados H2
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

### Console H2
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### JPA/Hibernate
```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Liquibase
```properties
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
spring.liquibase.enabled=true
```

## 🎯 Funcionalidades

- ✅ **Gerenciamento de Usuários**: CRUD completo e sistema de ranking
- ✅ **Tópicos de Aprendizado**: Criação e categorização por tema e nível
- ✅ **Sistema de Curtidas**: Os tópicos podem ser curtidos pelos usuários
- ✅ **Trilhas Personalizadas**: Criação manual de trilhas com múltiplos tópicos
- ✅ **Geração Automática**: Criação de trilhas baseadas em tema e nível
- ✅ **Cache Inteligente**: Sistema de cache com limpeza automática agendada
- ✅ **Documentação Automática**: Swagger/OpenAPI integrado
- ✅ **Migrations**: Controle de versão do banco com Liquibase



# 🧮 Dynamic Programming



### 1. 🏆 Fila de Prioridade com Heaps

#### Descrição
Uma **Fila de Prioridade (PriorityQueue)** é uma estrutura de dados que mantém elementos ordenados automaticamente, utilizando internamente uma estrutura de **Heap**. No Java, a `PriorityQueue` implementa um **Min-Heap** por padrão, onde o menor elemento (segundo o comparador definido) fica sempre no topo.

#### Aplicação no Projeto
No sistema, utilizamos uma `PriorityQueue` para gerenciar o **ranking de usuários** . A fila mantém os usuários ordenados automaticamente por suas pontuações e trilhas finalizadas, garantindo que o ranking esteja sempre atualizado sem necessidade de ordenação manual.

#### Vantagens
- **Complexidade O(log n)** para inserção e remoção
- **Complexidade O(1)** para acessar o elemento de maior prioridade
- Ordenação automática dos elementos
- Eficiente para grandes volumes de dados

#### Implementação no Sistema

**Arquivo : src/main/java/com/fiap/gs/demo/users/UserRankingService.java**

```java
    private PriorityQueue<UserRankingDTO> rankingQueue;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeRanking() {
        rankingQueue = new PriorityQueue<>(50);
        loadAllUsersIntoRanking();
    }
    @Transactional
    public void loadAllUsersIntoRanking() {
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
```

**Características da Implementação:**
- A `PriorityQueue` é inicializada com capacidade inicial de 50 elementos
- Método `offer()` insere novos usuários mantendo a ordenação (O(log n))
- O `UserRankingDTO` implementa `Comparable` para definir a ordem de prioridade
- Os usuários são ordenados por trilhas finalizadas e quantidade total de trilhas
- A estrutura é carregada na inicialização da aplicação via `@EventListener`

---

### 2. 🗂️ Tabela Hash (HashMap)

#### Descrição

#### Aplicação no Projeto
Implementamos um **sistema de cache customizado** usando `ConcurrentHashMap`, uma versão thread-safe da HashMap. O cache armazena resultados de consultas custosas com um tempo de vida (TTL) configurável, reduzindo drasticamente a carga no banco de dados.

#### Vantagens
- **Complexidade O(1)** para operações de busca, inserção e remoção
- Thread-safe com `ConcurrentHashMap`
- Suporta múltiplos caches independentes
- Controle de expiração por TTL (Time To Live)

#### Implementação no Sistema

**Arquivo: src/main/java/com/fiap/gs/demo/shared/cache/CacheService.java**

```java
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
```

**Características da Implementação:**
- **Estrutura aninhada**: `Map<String, Map<Object, CacheEntry<Object>>>` permite múltiplos caches independentes
- **ConcurrentHashMap**: Garante segurança em ambientes multi-thread
- **TTL automático**: Cada entrada possui tempo de expiração
- **Lazy loading**: Valores são carregados apenas quando necessário
- **Pattern Supplier**: Encapsula a lógica de busca de dados


---

### 3. 🕸️ Grafos (Relacionamentos N:N)

#### Descrição
Um **Grafo** é uma estrutura de dados composta por vértices (nós) e arestas (conexões). No contexto de banco de dados, os relacionamentos N:N (muitos-para-muitos) formam estruturas de grafos onde entidades podem se conectar entre si.

#### Aplicação no Projeto
Implementamos grafos em dois níveis:

1. **Nível de Dados**: Relacionamentos N:N entre Tópicos e Trilhas armazenados no banco de dados
2. **Nível de Algoritmo**: Algoritmo de travessia de grafo para gerar trilhas conectadas

#### Grafos no Modelo de Dados

##### Relacionamento N:N em Trilhas
**Arquivo:src/main/java/com/fiap/gs/demo/trilhas/Trilha.java**
```java
    @ManyToMany
    @JoinTable(
        name = "tb_trilha_topico",
        joinColumns = @JoinColumn(name = "id_trilha"),
        inverseJoinColumns = @JoinColumn(name = "id_topico")
    )
    @Builder.Default
    private Set<Topico> topicos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "tb_trilha_relacionada",
        joinColumns = @JoinColumn(name = "id_trilha"),
        inverseJoinColumns = @JoinColumn(name = "id_trilha_relacionada")
    )
    @Builder.Default
    private Set<Trilha> trilhasRelacionadas = new HashSet<>();
```

##### Relacionamento N:N em Tópicos

```java
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_topicos_relacionados",
        joinColumns = @JoinColumn(name = "id_topico"),
        inverseJoinColumns = @JoinColumn(name = "id_topico_relacionado")
    )
    private Set<Topico> topicosRelacionados;
```

**Estrutura do Grafo:**
- **Vértices**: Tópicos e Trilhas
- **Arestas**: Relacionamentos armazenados nas tabelas de junção
- **Tipo**: Grafo não-direcionado (relacionamentos bidirecionais)

### Algoritmo de Travessia de Grafo (BFS Modificado)

O sistema implementa um algoritmo de **Busca em Largura (BFS)** modificado para gerar uma trilha aleatóriamente com base em alguns parâmetros recebiso pelo usuário:

```java
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
```

**Fluxo do Algoritmo:**
1. Escolhe um tópico inicial (geralmente o mais curtido com relacionamentos)
2. Adiciona o tópico à fila e marca como visitado
3. Remove tópico da fila e adiciona ao resultado
4. Busca todos os tópicos relacionados (vizinhos no grafo)
5. Filtra vizinhos válidos e não visitados
6. Adiciona até 3 vizinhos aleatórios à fila
7. Repete até atingir o tamanho desejado da trilha

---

### 4. 🎲 Algoritmos Randômicos


#### Aplicação no Projeto
Implementamos múltiplos pontos de aleatoriedade no gerador de trilhas para garantir que cada trilha gerada seja única e diversificada, mesmo com os mesmos parâmetros de entrada.

#### Implementações Randômicas

##### 1. Seleção do Tópico Inicial

```java
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
```

**Estratégia:**
- Ordena tópicos por curtidas (qualidade)
- Seleciona aleatoriamente entre os 3 melhores (evita sempre o mesmo)
- Fallback aleatório se não houver tópicos com relacionamentos

##### 2. Tamanho Aleatório da Trilha

```java
        int targetSize = MIN_TOPICOS + random.nextInt(MAX_TOPICOS - MIN_TOPICOS + 1);
        targetSize = Math.min(targetSize, topicosDisponiveis.size());
```

**Características:**
- Tamanho varia entre 3 e 8 tópicos aleatoriamente
- Garante diversidade no tamanho das trilhas
- Limita ao máximo de tópicos disponíveis

##### 3. Shuffle de Tópicos Relacionados

```java
                Collections.shuffle(relacionadosValidos);
                int toAdd = Math.min(relacionadosValidos.size(), 3);
```

**Objetivo:**
- Embaralha os tópicos relacionados antes de selecioná-los
- Garante que a ordem de adição seja aleatória
- Evita sempre os mesmos caminhos no grafo

##### 4. Nome Aleatório da Trilha

```java
    private String generateTrilhaName(TemasEnum tema, NivelTopicoEnum nivel) {
        String[] prefixos = {"Trilha", "Jornada", "Caminho", "Curso", "Aprendizado"};
        String prefixo = prefixos[random.nextInt(prefixos.length)];
        
        return String.format("%s de %s - %s", 
                prefixo, 
                tema.getLabel(), 
                nivel.getLabel());
    }
```

**Variações:**
- 5 prefixos possíveis
- Gera nomes como: "Jornada de Tecnologia - Básico", "Curso de Sustentabilidade - Avançado"


---

