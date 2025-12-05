package com.projeto.aeroportos.controller;

import com.projeto.aeroportos.domain.Aeroporto;
import com.projeto.aeroportos.service.AeroportoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aeroportos")
public class AeroportoController {

    @Autowired
    private AeroportoService aeroportoService;

    // GET /api/v1/aeroportos - Obter todos os aeroportos
    @GetMapping
    public ResponseEntity<List<Aeroporto>> listarTodos() {
        List<Aeroporto> aeroportos = aeroportoService.listarTodos();
        return ResponseEntity.ok(aeroportos);
    }

    // GET /api/v1/aeroportos/{iata} - Obter um aeroporto pelo código IATA
    @GetMapping("/{iata}")
    public ResponseEntity<Aeroporto> buscarPorIata(@PathVariable String iata) {
        Aeroporto aeroporto = aeroportoService.buscarPorIata(iata);
        return ResponseEntity.ok(aeroporto);
    }

    // POST /api/v1/aeroportos - Adicionar um novo aeroporto
    @PostMapping
    public ResponseEntity<Aeroporto> criar(@Valid @RequestBody Aeroporto aeroporto) {
        Aeroporto aeroportoCriado = aeroportoService.criar(aeroporto);
        return ResponseEntity.status(HttpStatus.CREATED).body(aeroportoCriado);
    }

    // PUT /api/v1/aeroportos/{iata} - Atualizar um aeroporto existente
    @PutMapping("/{iata}")
    public ResponseEntity<Aeroporto> atualizar(
            @PathVariable String iata,
            @Valid @RequestBody Aeroporto aeroporto) {
        Aeroporto aeroportoAtualizado = aeroportoService.atualizar(iata, aeroporto);
        return ResponseEntity.ok(aeroportoAtualizado);
    }

    // DELETE /api/v1/aeroportos/{iata} - Excluir um aeroporto
    @DeleteMapping("/{iata}")
    public ResponseEntity<Void> deletar(@PathVariable String iata) {
        aeroportoService.deletar(iata);
        return ResponseEntity.noContent().build();
    }
}