package com.projeto.aeroportos.service;

import com.projeto.aeroportos.domain.Aeroporto;
import com.projeto.aeroportos.exception.AeroportoNaoEncontradoException;
import com.projeto.aeroportos.repository.AeroportoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AeroportoServiceTest {

    @Mock
    private AeroportoRepository aeroportoRepository;

    @InjectMocks
    private AeroportoService aeroportoService;

    private Aeroporto aeroportoGRU;
    private Aeroporto aeroportoCGH;

    @BeforeEach
    void setUp() {
        // Aeroporto Guarulhos
        aeroportoGRU = new Aeroporto(
            "Aeroporto Internacional de São Paulo/Guarulhos",
            "GRU",
            "São Paulo",
            "BR",
            -23.4356,
            -46.4731,
            750.0
        );
        aeroportoGRU.setId(1L);

        // Aeroporto Congonhas
        aeroportoCGH = new Aeroporto(
            "Aeroporto de Congonhas",
            "CGH",
            "São Paulo",
            "BR",
            -23.6261,
            -46.6564,
            802.0
        );
        aeroportoCGH.setId(2L);
    }

    // ========== TESTES DOS MÉTODOS ESTÁTICOS (exigidos no trabalho) ==========

    @Test
    void testConverterPesParaMetros_DeveRetornarValorCorreto() {
        // Arrange
        double pes = 1000.0;
        double metrosEsperados = 304.8;

        // Act
        double resultado = AeroportoService.converterPesParaMetros(pes);

        // Assert
        assertEquals(metrosEsperados, resultado, 0.001, 
            "1000 pés devem ser convertidos para 304.8 metros");
    }

    @Test
    void testConverterPesParaMetros_ComZeroPes() {
        // Arrange
        double pes = 0.0;

        // Act
        double resultado = AeroportoService.converterPesParaMetros(pes);

        // Assert
        assertEquals(0.0, resultado, 0.001,
            "0 pés devem ser convertidos para 0 metros");
    }

    @Test
    void testObterIsoPais_ComBrasil_DeveRetornarBR() {
        // Arrange
        String nomePais = "Brazil";

        // Act
        String resultado = AeroportoService.obterIsoPais(nomePais);

        // Assert
        assertEquals("BR", resultado,
            "País 'Brazil' deve retornar código ISO 'BR'");
    }

    @Test
    void testObterIsoPais_ComEstadosUnidos_DeveRetornarUS() {
        // Arrange
        String nomePais = "United States";

        // Act
        String resultado = AeroportoService.obterIsoPais(nomePais);

        // Assert
        assertEquals("US", resultado,
            "País 'United States' deve retornar código ISO 'US'");
    }

    @Test
    void testObterIsoPais_ComNomeDesconhecido_DeveRetornarInterrogacoes() {
        // Arrange
        String nomePais = "País Inexistente";

        // Act
        String resultado = AeroportoService.obterIsoPais(nomePais);

        // Assert
        assertEquals("??", resultado,
            "País desconhecido deve retornar '??'");
    }

    // ========== TESTES DOS MÉTODOS DE NEGÓCIO ==========

    @Test
    void testListarTodos_QuandoExistemAeroportos_DeveRetornarLista() {
        // Arrange
        List<Aeroporto> aeroportos = Arrays.asList(aeroportoGRU, aeroportoCGH);
        when(aeroportoRepository.findAll()).thenReturn(aeroportos);

        // Act
        List<Aeroporto> resultado = aeroportoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("GRU", resultado.get(0).getCodigoIata());
        assertEquals("CGH", resultado.get(1).getCodigoIata());
        verify(aeroportoRepository, times(1)).findAll();
    }

    @Test
    void testListarTodos_QuandoNaoExistemAeroportos_DeveRetornarListaVazia() {
        // Arrange
        when(aeroportoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Aeroporto> resultado = aeroportoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(aeroportoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorIata_QuandoAeroportoExiste_DeveRetornarAeroporto() {
        // Arrange
        when(aeroportoRepository.findByCodigoIata("GRU"))
            .thenReturn(Optional.of(aeroportoGRU));

        // Act
        Aeroporto resultado = aeroportoService.buscarPorIata("GRU");

        // Assert
        assertNotNull(resultado);
        assertEquals("GRU", resultado.getCodigoIata());
        assertEquals("Aeroporto Internacional de São Paulo/Guarulhos", resultado.getNome());
        verify(aeroportoRepository, times(1)).findByCodigoIata("GRU");
    }

    @Test
    void testBuscarPorIata_QuandoAeroportoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(aeroportoRepository.findByCodigoIata("XXX"))
            .thenReturn(Optional.empty());

        // Act & Assert
        AeroportoNaoEncontradoException exception = assertThrows(
            AeroportoNaoEncontradoException.class,
            () -> aeroportoService.buscarPorIata("XXX"),
            "Deveria lançar AeroportoNaoEncontradoException"
        );

        assertTrue(exception.getMessage().contains("XXX"));
        verify(aeroportoRepository, times(1)).findByCodigoIata("XXX");
    }

    @Test
    void testBuscarPorIata_DeveConverterParaMaiusculas() {
        // Arrange
        when(aeroportoRepository.findByCodigoIata("GRU"))
            .thenReturn(Optional.of(aeroportoGRU));

        // Act
        Aeroporto resultado = aeroportoService.buscarPorIata("gru");

        // Assert
        assertNotNull(resultado);
        verify(aeroportoRepository, times(1)).findByCodigoIata("GRU");
    }

    @Test
    void testCriar_ComDadosValidos_DeveSalvarAeroporto() {
        // Arrange
        when(aeroportoRepository.existsByCodigoIata("GRU")).thenReturn(false);
        when(aeroportoRepository.save(any(Aeroporto.class))).thenReturn(aeroportoGRU);

        // Act
        Aeroporto resultado = aeroportoService.criar(aeroportoGRU);

        // Assert
        assertNotNull(resultado);
        assertEquals("GRU", resultado.getCodigoIata());
        verify(aeroportoRepository, times(1)).existsByCodigoIata("GRU");
        verify(aeroportoRepository, times(1)).save(aeroportoGRU);
    }

    @Test
    void testCriar_ComIataDuplicado_DeveLancarExcecao() {
        // Arrange
        when(aeroportoRepository.existsByCodigoIata("GRU")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aeroportoService.criar(aeroportoGRU),
            "Deveria lançar IllegalArgumentException para IATA duplicado"
        );

        assertTrue(exception.getMessage().contains("já existe"));
        verify(aeroportoRepository, times(1)).existsByCodigoIata("GRU");
        verify(aeroportoRepository, never()).save(any(Aeroporto.class));
    }

    @Test
    void testCriar_DeveConverterIataParaMaiusculas() {
        // Arrange
        Aeroporto aeroportoComIataMinusculo = new Aeroporto(
            "Aeroporto Teste",
            "gru", // minúsculo
            "Teste",
            "BR",
            0.0, 0.0, 0.0
        );
        
        when(aeroportoRepository.existsByCodigoIata("GRU")).thenReturn(false);
        when(aeroportoRepository.save(any(Aeroporto.class))).thenAnswer(invocation -> {
            Aeroporto saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // Act
        Aeroporto resultado = aeroportoService.criar(aeroportoComIataMinusculo);

        // Assert
        assertEquals("GRU", resultado.getCodigoIata());
    }

    @Test
    void testAtualizar_ComDadosValidos_DeveAtualizarAeroporto() {
        // Arrange
        Aeroporto aeroportoAtualizado = new Aeroporto(
            "Novo Nome do Aeroporto",
            "GRU",
            "Nova Cidade",
            "US", // Mudou país
            -25.0, // Nova latitude
            -48.0, // Nova longitude
            500.0  // Nova altitude
        );

        when(aeroportoRepository.findByCodigoIata("GRU"))
            .thenReturn(Optional.of(aeroportoGRU));
        when(aeroportoRepository.save(any(Aeroporto.class))).thenReturn(aeroportoGRU);

        // Act
        Aeroporto resultado = aeroportoService.atualizar("GRU", aeroportoAtualizado);

        // Assert
        assertNotNull(resultado);
        verify(aeroportoRepository, times(1)).findByCodigoIata("GRU");
        verify(aeroportoRepository, times(1)).save(aeroportoGRU);
    }

    @Test
    void testDeletar_QuandoAeroportoExiste_DeveDeletar() {
        // Arrange
        when(aeroportoRepository.existsByCodigoIata("GRU")).thenReturn(true);

        // Act
        aeroportoService.deletar("GRU");

        // Assert
        verify(aeroportoRepository, times(1)).existsByCodigoIata("GRU");
        verify(aeroportoRepository, times(1)).deleteByCodigoIata("GRU");
    }

    @Test
    void testDeletar_QuandoAeroportoNaoExiste_DeveLancarExcecao() {
        // Arrange
        when(aeroportoRepository.existsByCodigoIata("XXX")).thenReturn(false);

        // Act & Assert
        assertThrows(
            AeroportoNaoEncontradoException.class,
            () -> aeroportoService.deletar("XXX"),
            "Deveria lançar exceção ao tentar deletar aeroporto inexistente"
        );

        verify(aeroportoRepository, times(1)).existsByCodigoIata("XXX");
        verify(aeroportoRepository, never()).deleteByCodigoIata(anyString());
    }
}