package com.fiap.gs.demo.topicos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fiap.gs.demo.topicos.dto.CreateTopicoDTO;
import com.fiap.gs.demo.topicos.dto.TopicoResponseDTO;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<TopicoResponseDTO> createTopico(@Valid @RequestBody CreateTopicoDTO createTopicoDTO) {
        TopicoResponseDTO response = topicoService.createTopico(createTopicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> getTopicoById(@PathVariable @NotNull Long id) {
        TopicoResponseDTO response = topicoService.getTopicoById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/curtir")
    public ResponseEntity<TopicoResponseDTO> curtirTopico(@PathVariable @NotNull Long id) {
        TopicoResponseDTO response = topicoService.curtirTopico(id);
        return ResponseEntity.ok(response);
    }
}

