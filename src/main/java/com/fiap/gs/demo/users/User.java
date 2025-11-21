package com.fiap.gs.demo.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fiap.gs.demo.trilhas.Trilha;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "tb_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "ds_nome")
    private String nome;

    @NotNull
    @NotBlank
    @Column(name = "ds_apelido")
    private String apelido;

    @NotNull
    @NotBlank
    @Email
    @Column(name = "ds_email", unique = true)
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    @Column(name = "ds_cpf", unique = true, length = 11)
    private String cpf;

    @NotNull    
    @Positive
    @Column(name = "ds_idade")
    private Integer idade;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Trilha> trilhas;
}

