package com.fiap.gs.demo.topicos.dto;

import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
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
public class CreateTopicoDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String nome;

    @NotNull(message = "Nível é obrigatório")
    private NivelTopicoEnum nivel;

    @NotNull(message = "Tema é obrigatório")
    private TemasEnum tema;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String conteudo;

    private String referencias;

    private Set<Long> idTopicosRelacionados;
}

