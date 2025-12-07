# API REST: Gerenciamento de Aeroportos

## 📋 Descrição do Projeto
API REST desenvolvida em Spring Boot para gerenciamento de aeroportos baseada no dataset OpenFlights. Esta API permite realizar operações CRUD completas sobre dados de aeroportos mundiais.

## 🎯 Objetivo
Desenvolver uma API REST completa seguindo os padrões RESTful para gerenciar o cadastro de aeroportos espalhados pelo mundo, utilizando dados do projeto OpenFlights.

## 📊 Dicionário de Dados
| Campo           | Tipo                        | Descrição                                    |
|-----------------|-----------------------------|----------------------------------------------|
| id_aeroporto    | Inteiro                     | Chave primária que identifica cada aeroporto |
| nome_aeroporto  | Texto                       | Nome do aeroporto                            |
| codigo_iata     | Texto (3 letras)            | Código aeroportuário IATA                    |
| cidade          | Texto                       | Cidade onde está localizado                  |
| codigo_pais_iso | Texto (2 letras)            | Código ISO 3166-1 do país                    |
| latitude        | Real                        | Latitude do aeroporto                        |
| longitude       | Real                        | Longitude do aeroporto                       |
| altitude        | Real                        | Altitude em metros                           |

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **H2 Database** (teste/desenvolvimento)
- **MySQL** (produção)
- **Maven**
- **JUnit 5**
- **Mockito**
- **Jakarta Validation**

## 🚀 Configuração do Ambiente

### Pré-requisitos
- Java JDK 17 ou superior
- Maven 3.6+
- Git

### Clone do Repositório
```bash
git clone https://github.com/danielbony/api-aeroportos-danielbony.git
cd api-aeroportos-danielbony


## ▶️ Como Executar a Aplicação

### Executar em desenvolvimento:
```bash
mvn spring-boot:run