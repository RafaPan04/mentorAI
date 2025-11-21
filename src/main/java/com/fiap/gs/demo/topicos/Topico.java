package com.fiap.gs.demo.topicos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tb_topicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "ds_nome")
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_nivel")
    private NivelTopicoEnum nivel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_tema")
    private TemasEnum tema;

    @NotNull
    @Column(name = "nr_curtidas")
    @Builder.Default
    private Integer curtidas = 0;

    @NotBlank
    @Column(name = "ds_conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "ds_referencias", columnDefinition = "TEXT")
    private String referencias;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_topicos_relacionados",
        joinColumns = @JoinColumn(name = "id_topico"),
        inverseJoinColumns = @JoinColumn(name = "id_topico_relacionado")
    )
    private Set<Topico> topicosRelacionados;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void adicionarCurtida() {
        this.curtidas++;
    }
}

