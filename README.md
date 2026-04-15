# Transactions API - Spring Boot

Sistema para gerenciamento de transações financeiras entre contas de usuários, com autenticação JWT, persistência em PostgreSQL e documentação via Swagger UI.

## Visão Geral

- API REST para controle financeiro:
  - `Autenticação` — registro e login de usuários com geração de token JWT
  - `Contas` — consulta de saldo e extrato de transações
  - `Transações` — realização de depósitos e transferências entre contas com suporte a chaves de idempotência
  - `Admin` — listagem de usuários e contas (requer perfil ADMIN)
- Segurança robusta com Spring Security e JWT
- Documentação interativa via Swagger OpenAPI
- Persistência em PostgreSQL com JPA/Hibernate
- Orquestração via Docker Compose

## Tecnologias

- Java `21`
- Spring Boot `4.0.5`
- Spring Security (JWT - Auth0)
- Spring Data JPA/Hibernate
- Spring Web
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- PostgreSQL
- Docker e Docker Compose
- Lombok
- Maven

## Serviços Utilizados

- GitHub
- Docker

## Pré-requisitos

- `Java 21` instalado
- `Docker` e `Docker Compose` instalados
- Portas disponíveis:
  - `5433` (banco de dados PostgreSQL)
  - `8080` (aplicação Spring Boot)
  - `5050` (pgAdmin opcional)

## Configuração de Ambiente

As configurações padrões estão em `src/main/resources/application.properties`. Você pode sobrescrever via variáveis de ambiente padrão do Spring (`SPRING_*`).

- `server.port=8080`
- `spring.datasource.url=jdbc:postgresql://localhost:5433/transactions`
- `spring.datasource.username=postgres`
- `spring.datasource.password=123456`
- `spring.jpa.hibernate.ddl-auto=update`
- `transactions.api.security.secret=seu_segredo_jwt_aqui`

## Subir Infraestrutura (Docker)

A aplicação possui um `docker-compose.yml` na raiz do projeto para subir o banco de dados e a própria aplicação:

```bash
# Sobe banco de dados, aplicação e pgAdmin
docker-compose up -d
```

## Executar a Aplicação (Localmente)

- Via Maven Wrapper (Windows):

```bash
./mvnw.cmd spring-boot:run
```

- Via Maven Wrapper (Linux/Mac):

```bash
./mvnw spring-boot:run
```

## Endpoints Principais

Acesse a documentação completa em: `http://localhost:8080/swagger-ui/index.html`

- **Autenticação**
  - `POST /api/auth/register` — registra um novo usuário
  - `POST /api/auth/login` — realiza o login e retorna o token JWT

- **Transações**
  - `POST /api/transactions/deposit` — realiza um depósito (requer autenticação)
  - `POST /api/transactions/transfer` — realiza uma transferência entre contas (requer autenticação)

- **Contas**
  - `GET /api/accounts/{id}/balance` — consulta o saldo da conta
  - `GET /api/accounts/{id}/transactions` — lista o extrato da conta

- **Usuários**
  - `GET /api/users` — lista todos os usuários (requer perfil ADMIN)

- **Saúde**
  - `GET /api/health` — verifica o estado da aplicação

## Documentação Swagger

A documentação interativa (Swagger UI) está disponível em:
`http://localhost:8080/swagger-ui/index.html`

## Observações de Segurança

- Não versione segredos reais no `application.properties`. Utilize variáveis de ambiente para `TRANSACTIONS_API_SECURITY_SECRET` em produção.
- O header `Idempotency-Key` é suportado nos endpoints de transação para evitar duplicidade de operações em caso de falhas de rede.

## Versionamento

- `1.0.0`

## Autores

- `https://www.linkedin.com/in/luizfernando-java-developer/`

Obrigado por visitar e bons códigos!
