package com.projeto.aeroportos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "aeroporto")
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroporto")
    private Long id;

    @NotBlank(message = "Nome do aeroporto é obrigatório")
    @Size(max = 255, message = "Nome do aeroporto deve ter no máximo 255 caracteres")
    @Column(name = "nome_aeroporto", nullable = false)
    private String nome;

    @NotBlank(message = "Código IATA é obrigatório")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Código IATA deve ter exatamente 3 letras maiúsculas")
    @Column(name = "codigo_iata", unique = true, nullable = false, length = 3)
    private String codigoIata;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Column(name = "cidade", nullable = false)
    private String cidade;

    @NotBlank(message = "Código do país é obrigatório")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Código do país deve ter exatamente 2 letras maiúsculas (ISO 3166-1)")
    @Column(name = "codigo_pais_iso", nullable = false, length = 2)
    private String codigoPaisIso;

    @NotNull(message = "Latitude é obrigatória")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude é obrigatória")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull(message = "Altitude é obrigatória")
    @Min(value = 0, message = "Altitude não pode ser negativa")
    @Column(name = "altitude", nullable = false)
    private Double altitude;

    // Construtores
    public Aeroporto() {
    }

    public Aeroporto(String nome, String codigoIata, String cidade, 
                    String codigoPaisIso, Double latitude, Double longitude, Double altitude) {
        this.nome = nome;
        this.codigoIata = codigoIata;
        this.cidade = cidade;
        this.codigoPaisIso = codigoPaisIso;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    // Getters e Setters (vou escrever só os principais para economizar espaço)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCodigoIata() { return codigoIata; }
    public void setCodigoIata(String codigoIata) { this.codigoIata = codigoIata; }
    
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    
    public String getCodigoPaisIso() { return codigoPaisIso; }
    public void setCodigoPaisIso(String codigoPaisIso) { this.codigoPaisIso = codigoPaisIso; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Double getAltitude() { return altitude; }
    public void setAltitude(Double altitude) { this.altitude = altitude; }
}