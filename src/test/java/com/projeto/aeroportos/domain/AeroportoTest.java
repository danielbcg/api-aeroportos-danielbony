package com.projeto.aeroportos.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AeroportoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCriarAeroporto_ComDadosValidos_NaoDeveTerViolacoes() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto(
            "Aeroporto Internacional",
            "GRU",
            "São Paulo",
            "BR",
            -23.4356,
            -46.4731,
            750.0
        );

        // Act
        Set<ConstraintViolation<Aeroporto>> violations = validator.validate(aeroporto);

        // Assert
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    void testValidacao_NomeVazio_DeveViolar() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto(
            "", // Nome vazio
            "GRU",
            "São Paulo",
            "BR",
            -23.4356,
            -46.4731,
            750.0
        );

        // Act
        Set<ConstraintViolation<Aeroporto>> violations = validator.validate(aeroporto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Nome do aeroporto é obrigatório")));
    }

    @Test
    void testValidacao_CodigoIataInvalido_DeveViolar() {
        // Teste 1: IATA com 2 letras
        Aeroporto aeroporto1 = new Aeroporto(
            "Aeroporto Teste",
            "AB", // Apenas 2 letras
            "Cidade",
            "BR",
            0.0, 0.0, 0.0
        );

        // Teste 2: IATA com 4 letras
        Aeroporto aeroporto2 = new Aeroporto(
            "Aeroporto Teste",
            "ABCD", // 4 letras
            "Cidade",
            "BR",
            0.0, 0.0, 0.0
        );

        // Teste 3: IATA com números
        Aeroporto aeroporto3 = new Aeroporto(
            "Aeroporto Teste",
            "AB1", // Contém número
            "Cidade",
            "BR",
            0.0, 0.0, 0.0
        );

        // Act & Assert
        Set<ConstraintViolation<Aeroporto>> violations1 = validator.validate(aeroporto1);
        assertFalse(violations1.isEmpty());
        assertTrue(violations1.stream()
            .anyMatch(v -> v.getMessage().contains("Código IATA deve ter exatamente 3 letras")));

        Set<ConstraintViolation<Aeroporto>> violations2 = validator.validate(aeroporto2);
        assertFalse(violations2.isEmpty());

        Set<ConstraintViolation<Aeroporto>> violations3 = validator.validate(aeroporto3);
        assertFalse(violations3.isEmpty());
    }

    @Test
    void testValidacao_CodigoPaisInvalido_DeveViolar() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto(
            "Aeroporto Teste",
            "GRU",
            "Cidade",
            "BRA", // 3 letras (deveria ser 2)
            0.0, 0.0, 0.0
        );

        // Act
        Set<ConstraintViolation<Aeroporto>> violations = validator.validate(aeroporto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Código do país deve ter exatamente 2 letras")));
    }

    @Test
    void testValidacao_AltitudeNegativa_DeveViolar() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto(
            "Aeroporto Teste",
            "GRU",
            "Cidade",
            "BR",
            0.0, 0.0, -100.0 // Altitude negativa
        );

        // Act
        Set<ConstraintViolation<Aeroporto>> violations = validator.validate(aeroporto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Altitude não pode ser negativa")));
    }

    @Test
    void testValidacao_LatitudeNula_DeveViolar() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto(
            "Aeroporto Teste",
            "GRU",
            "Cidade",
            "BR",
            null, // Latitude nula
            0.0, 0.0
        );

        // Act
        Set<ConstraintViolation<Aeroporto>> violations = validator.validate(aeroporto);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Latitude é obrigatória")));
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Aeroporto aeroporto = new Aeroporto();

        // Act
        aeroporto.setId(1L);
        aeroporto.setNome("Teste");
        aeroporto.setCodigoIata("ABC");
        aeroporto.setCidade("Cidade");
        aeroporto.setCodigoPaisIso("BR");
        aeroporto.setLatitude(10.0);
        aeroporto.setLongitude(20.0);
        aeroporto.setAltitude(30.0);

        // Assert
        assertEquals(1L, aeroporto.getId());
        assertEquals("Teste", aeroporto.getNome());
        assertEquals("ABC", aeroporto.getCodigoIata());
        assertEquals("Cidade", aeroporto.getCidade());
        assertEquals("BR", aeroporto.getCodigoPaisIso());
        assertEquals(10.0, aeroporto.getLatitude());
        assertEquals(20.0, aeroporto.getLongitude());
        assertEquals(30.0, aeroporto.getAltitude());
    }
}