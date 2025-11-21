package com.fiap.gs.demo.topicos;

import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
            
    List<Topico> findByTemaAndNivel(TemasEnum tema, NivelTopicoEnum nivel);
    
    @Query("SELECT t FROM Topico t LEFT JOIN FETCH t.topicosRelacionados WHERE t.id = :id")
    Optional<Topico> findByIdWithRelacionados(@Param("id") Long id);
}

