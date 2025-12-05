package com.projeto.aeroportos.repository;

import com.projeto.aeroportos.domain.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {
    
    // Busca por código IATA
    Optional<Aeroporto> findByCodigoIata(String codigoIata);
    
    // Verifica se existe pelo código IATA
    boolean existsByCodigoIata(String codigoIata);
    
    // Deleta por código IATA
    void deleteByCodigoIata(String codigoIata);
}