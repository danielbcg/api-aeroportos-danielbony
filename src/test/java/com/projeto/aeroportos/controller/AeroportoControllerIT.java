package com.projeto.aeroportos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.aeroportos.domain.Aeroporto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class AeroportoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Aeroporto aeroportoTeste;

    @BeforeEach
    void setUp() {
        aeroportoTeste = new Aeroporto(
            "Aeroporto Internacional de Teste",
            "TST",
            "Cidade Teste",
            "BR",
            -23.5505,
            -46.6333,
            760.0
        );
    }

    @Test
    @Order(1)
    void testCriarAeroporto_DeveRetornar201() throws Exception {
        // Arrange
        String aeroportoJson = objectMapper.writeValueAsString(aeroportoTeste);

        // Act & Assert
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Aeroporto Internacional de Teste"))
                .andExpect(jsonPath("$.codigoIata").value("TST"))
                .andExpect(jsonPath("$.cidade").value("Cidade Teste"))
                .andExpect(jsonPath("$.codigoPaisIso").value("BR"));
    }

    @Test
    @Order(2)
    void testBuscarAeroportoPorIata_DeveRetornar200() throws Exception {
        // Arrange - Primeiro cria
        String aeroportoJson = objectMapper.writeValueAsString(aeroportoTeste);
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoJson));

        // Act & Assert - Depois busca
        mockMvc.perform(get("/api/v1/aeroportos/TST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoIata").value("TST"))
                .andExpect(jsonPath("$.nome").value("Aeroporto Internacional de Teste"));
    }

    @Test
    @Order(3)
    void testBuscarAeroportoInexistente_DeveRetornar404() throws Exception {
        mockMvc.perform(get("/api/v1/aeroportos/XXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    void testAtualizarAeroporto_DeveRetornar200() throws Exception {
        // Arrange - Primeiro cria
        String aeroportoJson = objectMapper.writeValueAsString(aeroportoTeste);
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoJson));

        // Cria objeto atualizado
        Aeroporto aeroportoAtualizado = new Aeroporto(
            "Aeroporto Atualizado",
            "TST", // Mesmo IATA
            "Nova Cidade",
            "US", // Novo país
            -25.0,
            -48.0,
            500.0
        );
        String aeroportoAtualizadoJson = objectMapper.writeValueAsString(aeroportoAtualizado);

        // Act & Assert - Atualiza
        mockMvc.perform(put("/api/v1/aeroportos/TST")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoAtualizadoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Aeroporto Atualizado"))
                .andExpect(jsonPath("$.codigoPaisIso").value("US"));
    }

    @Test
    @Order(5)
    void testDeletarAeroporto_DeveRetornar204() throws Exception {
        // Arrange - Primeiro cria
        String aeroportoJson = objectMapper.writeValueAsString(aeroportoTeste);
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoJson));

        // Act & Assert - Deleta
        mockMvc.perform(delete("/api/v1/aeroportos/TST"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    void testBuscarAposDeletar_DeveRetornar404() throws Exception {
        // Act & Assert - Tenta buscar após deletar (deve falhar)
        mockMvc.perform(get("/api/v1/aeroportos/TST"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void testListarTodosAeroportos_DeveRetornar200() throws Exception {
        // Cria dois aeroportos
        Aeroporto aeroporto1 = new Aeroporto("Aeroporto 1", "AAA", "Cidade 1", "BR", 0.0, 0.0, 0.0);
        Aeroporto aeroporto2 = new Aeroporto("Aeroporto 2", "BBB", "Cidade 2", "US", 1.0, 1.0, 1.0);

        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aeroporto1)));
        
        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aeroporto2)));

        // Busca todos
        mockMvc.perform(get("/api/v1/aeroportos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].codigoIata", containsInAnyOrder("AAA", "BBB")));
    }

    @Test
    void testCriarAeroportoComDadosInvalidos_DeveRetornar400() throws Exception {
        // Aeroporto com IATA inválido (4 letras)
        Aeroporto aeroportoInvalido = new Aeroporto(
            "Aeroporto Inválido",
            "ABCD", // 4 letras - inválido!
            "Cidade",
            "BR",
            0.0, 0.0, 0.0
        );

        String aeroportoInvalidoJson = objectMapper.writeValueAsString(aeroportoInvalido);

        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoInvalidoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCriarAeroportoComAltitudeNegativa_DeveRetornar400() throws Exception {
        // Aeroporto com altitude negativa
        Aeroporto aeroportoInvalido = new Aeroporto(
            "Aeroporto Inválido",
            "NEG",
            "Cidade",
            "BR",
            0.0, 0.0, -100.0 // Altitude negativa - inválido!
        );

        String aeroportoInvalidoJson = objectMapper.writeValueAsString(aeroportoInvalido);

        mockMvc.perform(post("/api/v1/aeroportos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aeroportoInvalidoJson))
                .andExpect(status().isBadRequest());
    }
}