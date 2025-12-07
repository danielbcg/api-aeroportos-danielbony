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
        String iataUpper = codigoIata.toUpperCase();
        return aeroportoRepository.findByCodigoIata(iataUpper)
                .orElseThrow(() -> new AeroportoNaoEncontradoException(codigoIata));
    }

    // Criar novo aeroporto
    @Transactional
    public Aeroporto criar(Aeroporto aeroporto) {
        // Converte IATA para maiúsculas
        String iataUpper = aeroporto.getCodigoIata().toUpperCase();
        aeroporto.setCodigoIata(iataUpper);
        
        // Converte código país para maiúsculas
        aeroporto.setCodigoPaisIso(aeroporto.getCodigoPaisIso().toUpperCase());
        
        // IMPORTANTE: Converte altitude de PÉS para METROS
        // O usuário envia em metros, mas se viesse do CSV estaria em pés
        // Para ser consistente, mantemos em metros
        // Se no futuro quisermos importar CSV, converteremos aqui
        // aeroporto.setAltitude(converterPesParaMetros(aeroporto.getAltitude()));
        
        // Valida se já existe
        if (aeroportoRepository.existsByCodigoIata(iataUpper)) {
            throw new IllegalArgumentException("Aeroporto com código IATA '" + iataUpper + "' já existe.");
        }
        
        return aeroportoRepository.save(aeroporto);
    }

    // Atualizar aeroporto
    @Transactional
    public Aeroporto atualizar(String codigoIata, Aeroporto aeroportoAtualizado) {
        // Busca aeroporto existente (já lança exceção se não existir)
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
        String iataUpper = codigoIata.toUpperCase();
        
        // Primeiro verifica se existe (mais eficiente)
        if (!aeroportoRepository.existsByCodigoIata(iataUpper)) {
            throw new AeroportoNaoEncontradoException(codigoIata);
        }
        
        // Deleta por IATA
        aeroportoRepository.deleteByCodigoIata(iataUpper);
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
            case "united kingdom", "uk", "reino unido" -> "GB";
            case "canada" -> "CA";
            case "mexico", "méxico" -> "MX";
            case "australia", "austrália" -> "AU";
            default -> "??";
        };
    }
}