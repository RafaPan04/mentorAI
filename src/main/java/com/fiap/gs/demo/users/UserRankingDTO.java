package com.fiap.gs.demo.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRankingDTO implements Comparable<UserRankingDTO> {
    
    private Long id;
    private String nome;
    private String apelido;
    private Long trilhasFinalizadas;
    private Long quantidadeTrilhas;

    @Override
    public int compareTo(UserRankingDTO other) {
  
        int comparacao = Long.compare(other.trilhasFinalizadas, this.trilhasFinalizadas);
        
        
        if (comparacao == 0) {
            return this.nome.compareTo(other.nome);
        }
        
        return comparacao;
    }
}

