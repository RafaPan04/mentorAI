package com.fiap.gs.demo.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder    
public class UserResponseDTO {

    private Long id;
    private String nome;
    private String apelido;
    private String email;
    private Integer idade;

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getApelido(),
                user.getEmail(),
                user.getIdade()
        );
    }
}

