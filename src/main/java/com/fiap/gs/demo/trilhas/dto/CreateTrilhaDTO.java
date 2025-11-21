package com.fiap.gs.demo.trilhas.dto;

import com.fiap.gs.demo.trilhas.enums.StatusTrilhaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrilhaDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String nome;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long idUser;

    private Set<Long> idTopicos;

    private Set<Long> idTrilhasRelacionadas;
}

