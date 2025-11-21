package com.fiap.gs.demo.users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Apelido é obrigatório")
    @Size(min = 2, max = 50, message = "Apelido deve ter entre 2 e 50 caracteres")
    private String apelido;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 18, message = "Idade deve ser maior que 17 anos")
    private Integer idade;
}

