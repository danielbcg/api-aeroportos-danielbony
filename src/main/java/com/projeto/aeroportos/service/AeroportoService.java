package com.projeto.aeroportos.service;

import com.projeto.aeroportos.domain.Aeroporto;
import com.projeto.aeroportos.exception.AeroportoNaoEncontradoException;
import com.projeto.aeroportos.repository.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AeroportoService {

    @Autowired
    private AeroportoRepository aeroportoRepository;

    // Listar todos aeroportos
    public List<Aeroporto> listarTodos() {
        return aeroportoRepository.findAll();
    }

    // Buscar por IATA
    public Aeroporto buscarPorIata(String codigoIata) {
        return aeroportoRepository.findByCodigoIata(codigoIata.toUpperCase())
                .orElseThrow(() -> new AeroportoNaoEncontradoException(codigoIata));
    }

    // Criar novo aeroporto
    @Transactional
    public Aeroporto criar(Aeroporto aeroporto) {
        // Converte IATA para maiúsculas
        aeroporto.setCodigoIata(aeroporto.getCodigoIata().toUpperCase());
        
        // Valida se já existe
        if (aeroportoRepository.existsByCodigoIata(aeroporto.getCodigoIata())) {
            throw new IllegalArgumentException("Aeroporto com código IATA '" + aeroporto.getCodigoIata() + "' já existe.");
        }
        
        return aeroportoRepository.save(aeroporto);
    }

    // Atualizar aeroporto
    @Transactional
    public Aeroporto atualizar(String codigoIata, Aeroporto aeroportoAtualizado) {
        Aeroporto aeroportoExistente = buscarPorIata(codigoIata);
        
        // Atualiza campos (NÃO atualiza o código IATA!)
        aeroportoExistente.setNome(aeroportoAtualizado.getNome());
        aeroportoExistente.setCidade(aeroportoAtualizado.getCidade());
        aeroportoExistente.setCodigoPaisIso(aeroportoAtualizado.getCodigoPaisIso().toUpperCase());
        aeroportoExistente.setLatitude(aeroportoAtualizado.getLatitude());
        aeroportoExistente.setLongitude(aeroportoAtualizado.getLongitude());
        aeroportoExistente.setAltitude(aeroportoAtualizado.getAltitude());
        
        return aeroportoRepository.save(aeroportoExistente);
    }

    // Deletar aeroporto
    @Transactional
    public void deletar(String codigoIata) {
        if (!aeroportoRepository.existsByCodigoIata(codigoIata.toUpperCase())) {
            throw new AeroportoNaoEncontradoException(codigoIata);
        }
        aeroportoRepository.deleteByCodigoIata(codigoIata.toUpperCase());
    }

    // ========== MÉTODOS PARA TESTES (exigidos no trabalho) ==========
    
    // Método para converter pés para metros
    public static double converterPesParaMetros(double pes) {
        return pes * 0.3048;
    }

    // Método para obter código ISO do país
    public static String obterIsoPais(String nomePais) {
        // Mapeamento simples - na prática usaria uma API ou banco de dados
        return switch (nomePais.toLowerCase()) {
            case "brazil", "brasil" -> "BR";
            case "united states", "usa", "estados unidos" -> "US";
            case "portugal" -> "PT";
            case "spain", "espanha" -> "ES";
            case "france", "frança" -> "FR";
            case "germany", "alemanha" -> "DE";
            case "italy", "itália" -> "IT";
            case "japan", "japão" -> "JP";
            case "argentina" -> "AR";
            case "chile" -> "CL";
            default -> "??";
        };
    }
}