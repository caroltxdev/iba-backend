# Ĩbá Backend - Sistema de Monitoramento Ambiental Comunitário

Backend completo em Java 21 + Spring Boot 3 para registro e consulta de ocorrências ambientais.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.2.1
- Spring Data JPA (Hibernate)
- PostgreSQL 16
- Flyway (migrations)
- Bean Validation (Jakarta)
- springdoc-openapi (Swagger)
- OpenPDF
- Lombok
- Testcontainers (testes)

## 📋 Requisitos

- Java 21+
- Maven 3.8+
- Docker & Docker Compose

## 🐳 Como Rodar

### 1. Subir o banco de dados
```bash
docker-compose up -d
```

Isso iniciará:
- PostgreSQL na porta 5432
- pgAdmin na porta 5050 (http://localhost:5050)
  - Email: admin@iba.com
  - Senha: admin

### 2. Rodar a aplicação
```bash
mvn clean install
mvn spring-boot:run
```

Ou com profile de desenvolvimento:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

A API estará disponível em: **http://localhost:8080**

### 3. Acessar a documentação Swagger

Abra no navegador: **http://localhost:8080/swagger-ui.html**

## 📚 Endpoints da API

Base URL: `http://localhost:8080/api`

### Ocorrências

#### Criar Ocorrência
```bash
curl -X POST http://localhost:8080/api/occurrences \
  -H "Content-Type: application/json" \
  -d '{
    "type": "QUEIMADA",
    "date": "2024-01-15",
    "description": "Queimada detectada próximo à reserva",
    "latitude": -15.123456,
    "longitude": -47.654321,
    "photoUrl": "https://example.com/photo.jpg"
  }'
```

#### Listar Ocorrências (sem filtros)
```bash
curl http://localhost:8080/api/occurrences
```

#### Listar Ocorrências (com filtros)
```bash
curl "http://localhost:8080/api/occurrences?type=QUEIMADA&start=2024-01-01&end=2024-12-31"
```

#### Buscar Ocorrência por ID
```bash
curl http://localhost:8080/api/occurrences/{id}
```

### Estatísticas

#### Dashboard (Summary)
```bash
curl "http://localhost:8080/api/stats/summary?start=2024-01-01&end=2024-12-31"
```

Resposta:
```json
{
  "total": 45,
  "byType": [
    {"type": "QUEIMADA", "count": 20},
    {"type": "DESMATAMENTO", "count": 15},
    {"type": "POLUICAO", "count": 10}
  ],
  "byMonth": [
    {"month": "2024-01", "count": 12},
    {"month": "2024-02", "count": 18},
    {"month": "2024-03", "count": 15}
  ]
}
```

### Relatórios

#### Gerar PDF
```bash
curl "http://localhost:8080/api/reports/pdf?start=2024-01-01&end=2024-12-31" \
  --output relatorio.pdf
```

#### Gerar PDF filtrado por tipo
```bash
curl "http://localhost:8080/api/reports/pdf?start=2024-01-01&end=2024-12-31&type=QUEIMADA" \
  --output relatorio-queimadas.pdf
```

## 🧪 Executar Testes
```bash
mvn test
```

Os testes incluem:
- Testes unitários do service
- Testes de integração com MockMvc
- Testes com Testcontainers (PostgreSQL)

## 📊 Modelo de Dados

### Occurrence

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | UUID | Identificador único (gerado automaticamente) |
| type | ENUM | Tipo da ocorrência (QUEIMADA, DESMATAMENTO, POLUICAO, GARIMPO, OUTROS) |
| date | DATE | Data da ocorrência |
| description | VARCHAR(280) | Descrição (5-280 caracteres) |
| latitude | NUMERIC(9,6) | Latitude (-90 a 90) |
| longitude | NUMERIC(9,6) | Longitude (-180 a 180) |
| photo_url | VARCHAR(500) | URL da foto (opcional) |
| created_at | TIMESTAMP | Data de criação (gerado automaticamente) |

## 🔧 Configuração

Edite `src/main/resources/application.yml` para personalizar:

- Porta do servidor
- Configurações do banco de dados
- Configurações do Flyway
- Configurações do JPA/Hibernate

## 📦 Estrutura do Projeto
````
src/
├── main/
│   ├── java/com/iba/
│   │   ├── config/          # Configurações (CORS, Swagger)
│   │   ├── controller/      # Controllers REST
│   │   ├── domain/          # Entidades e Enums
│   │   ├── dto/             # DTOs (request/response/projection)
│   │   ├── exception/       # Tratamento de exceções
│   │   ├── mapper/          # Mapeadores
│   │   ├── repository/      # Repositories JPA
│   │   └── service/         # Serviços
│   └── resources/
│       ├── db/migration/    # Scripts Flyway
│       └── application.yml  # Configurações
└── test/  
