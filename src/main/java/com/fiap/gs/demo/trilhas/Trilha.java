package com.fiap.gs.demo.trilhas;

import com.fiap.gs.demo.topicos.Topico;
import com.fiap.gs.demo.trilhas.enums.StatusTrilhaEnum;
import com.fiap.gs.demo.users.User;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_trilhas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trilha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "ds_nome")
    private String nome;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "id_user")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_status", nullable = false)
    @Builder.Default
    private StatusTrilhaEnum status = StatusTrilhaEnum.CRIADA;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public boolean isFinalizada() {
        return this.status == StatusTrilhaEnum.FINALIZADA;
    }

    public void finalizarTrilha() {
        this.status = StatusTrilhaEnum.FINALIZADA;
    }
}

