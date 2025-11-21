package com.fiap.gs.demo.trilhas;

import com.fiap.gs.demo.shared.TemasEnum;
import com.fiap.gs.demo.topicos.enums.NivelTopicoEnum;
import com.fiap.gs.demo.trilhas.dto.CreateTrilhaDTO;
import com.fiap.gs.demo.trilhas.dto.SampleTrilhaDTO;
import com.fiap.gs.demo.trilhas.dto.TrilhaResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trilhas")
public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @PostMapping
    public ResponseEntity<TrilhaResponseDTO> createTrilha(@Valid @RequestBody CreateTrilhaDTO createTrilhaDTO) {
        TrilhaResponseDTO response = trilhaService.createTrilha(createTrilhaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<SampleTrilhaDTO>> getSampleTrilhas() {
        List<SampleTrilhaDTO> response = trilhaService.getSampleTrilhas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrilhaResponseDTO> getTrilhaById(@PathVariable @NotNull Long id) {
        TrilhaResponseDTO response = trilhaService.getTrilhaById(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/random-generate")
    public ResponseEntity<TrilhaResponseDTO> generateRandomTrilha(
            @RequestParam @NotNull Long userId,
            @RequestParam @NotNull TemasEnum tema,
            @RequestParam @NotNull NivelTopicoEnum nivel) {
        
        TrilhaResponseDTO response = trilhaService.generateRandomTrilha(userId, tema, nivel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

