# Event Hub API

API REST para gerenciamento de eventos e venda de ingressos, desenvolvida com foco em escalabilidade, integridade de
dados e boas práticas de Engenharia de Software.

---

## 🛠 Tecnologias Utilizadas

| Tecnologia            | Finalidade                |
|-----------------------|---------------------------|
| **Java 17+**          | Linguagem principal       |
| **Spring Boot 3+**    | Framework web             |
| **PostgreSQL**        | Banco de dados relacional |
| **Docker & Compose**  | Containerização           |
| **MapStruct**         | Mapeamento DTO ↔ Entity   |
| **Lombok**            | Redução de boilerplate    |
| **OpenAPI (Swagger)** | Documentação interativa   |
| **JUnit 5 & Mockito** | Testes Unitários          |
| **React 19+**         | Interface web (Frontend)  |
| **TypeScript**        | Tipagem estática          |
| **Tailwind CSS**      | Estilização               |

---

## 💡 Decisões Técnicas e Arquitetura

### 1. Encapsulamento e Desacoplamento (SOLID)

A lógica de negócios foi estritamente segregada. A `IngressoService` atua como orquestradora e não acessa o
`EventoRepository` diretamente.

* **Decisão:** A responsabilidade de verificar disponibilidade e decrementar vagas foi centralizada no método
  `EventoService.validarEReservarVaga()`.
* **Benefício:** Isso respeita o *Single Responsibility Principle (SRP)* e o *Dependency Inversion Principle (DIP)*. Se
  a regra de capacidade mudar (ex: filas de espera), a alteração fica isolada no domínio de Eventos, sem quebrar a
  lógica de vendas.

### 2. Performance e Otimização de Queries

O fluxo de compra foi otimizado para reduzir *round-trips* ao banco de dados.

* **Estratégia:** Ao reservar uma vaga, o método retorna a instância atualizada do `Evento`. Isso evita que a
  `IngressoService` precise fazer um novo `SELECT` para associar o evento ao ingresso, garantindo atomicidade e
  performance em uma única transação.

### 3. PostgreSQL vs H2

Escolhido para simular um ambiente de produção real. O H2 (em memória) esconderia problemas de concorrência que o
PostgreSQL expõe, permitindo testar comportamentos reais de locks e transações.

### 4. MapStruct & Records

* **MapStruct:** Preferido por gerar código em tempo de compilação (type-safe) e alta performance, evitando o *overhead*
  de Reflection em tempo de execução.
* **Records:** Utilizados para DTOs por garantirem imutabilidade e clareza na transferência de dados.

### 5. Tratamento de Erros Centralizado

Implementado um `GlobalExceptionHandler` (`@ControllerAdvice`) que captura exceções de negócio (`NegocioException`) e de
sistema, retornando respostas JSON padronizadas (RFC 7807 problem details) em vez de stack traces vazados.

### 6. The 12-Factor App

* **Configurações:** Credenciais e portas são injetadas via variáveis de ambiente.
* **Descartabilidade:** Aplicação suporta *Graceful Shutdown*.
* **Logs:** Logs estruturados na saída padrão (`stdout`), prontos para coleta por ferramentas de observabilidade.

---

## 🧪 Testes Unitários

Os testes foram desenhados com **JUnit 5** e **Mockito**, focando no comportamento e interação entre as camadas.

Destaque para a estratégia de testes da `IngressoService`:

* **Mock de Serviços:** Não mockamos apenas o Repositório, mas sim a `EventoService`. Isso isola o teste da unidade de
  compra, confiando no contrato da interface.
* **Cenários Cobertos:**
    1. Compra com sucesso (Orquestração correta entre serviços).
    2. Tentativa de compra sem vagas (Validação de exceção de negócio).
    3. Participante ou Evento inexistente (Tratamento de erros de integridade).

Testes unitários implementados com **JUnit 5** e **Mockito**, focados na `IngressoService` conforme requisito do nível
Pleno.

Os testes cobrem os seguintes cenários do método `realizarCompra`:

| Cenário                     | Resultado Esperado                             |
|-----------------------------|------------------------------------------------|
| Compra com sucesso          | Capacidade decrementada em 1, ingresso salvo   |
| Evento sem capacidade       | `NegocioException` lançada, ingresso não salvo |
| Evento não encontrado       | `EntityNotFoundException` lançada              |
| Participante não encontrado | `EntityNotFoundException` lançada              |

Os testes são executados automaticamente durante o `docker compose up --build`. Para rodá-los manualmente:

```bash
./mvnw test
```

---

## Como Rodar

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado.

### Passo a Passo

**1. Clone o repositório:**

```bash
https://github.com/guiklinke/event-hub.git
cd event-hub
```

**2. Suba a aplicação (API + Banco de Dados):**

```bash
docker compose up --build
```

**3.** Aguarde os logs indicarem que a aplicação iniciou. A API estará disponível em:

```
http://localhost:8080/api
```

---

## 📄 Documentação da API (Swagger)

A documentação interativa com todos os endpoints, schemas e exemplos de requisição está disponível em:

👉 [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

---

## Como Testar

### 1. Criar um Evento

`POST /api/events`

```json
{
  "nome": "Java Summit 2026",
  "data": "25/12/2026 14:00",
  "local": "Auditório Principal",
  "capacidade": 100
}
```

### 2. Cadastrar um Participante

`POST /api/participants`

```json
{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "cpf": "123.456.789-00"
}
```

### 3. Comprar um Ingresso

`POST /api/tickets`

```json
{
  "eventoId": 1,
  "participanteId": 1
}
```

### Outros Endpoints Disponíveis

| Método   | Rota                                        | Descrição                                     |
|----------|---------------------------------------------|-----------------------------------------------|
| `GET`    | `/api/events`                               | Lista todos os eventos (paginado e ordenável) |
| `GET`    | `/api/events/{id}`                          | Busca detalhada de um evento                  |
| `PUT`    | `/api/events/{id}`                          | Atualiza um evento                            |
| `DELETE` | `/api/events/{id}`                          | Cancela um evento                             |
| `POST`   | `/api/participants`                         | Cadastra um participante                      |
| `GET`    | `/api/tickets/participants/{participantId}` | Busca ingressos de um participante            |

---

### 4. Verificar Saúde da API (Actuator)

`GET /actuator/health`

---

## Frontend

A interface web desenvolvida com **React + TypeScript** estará disponível em:
```
http://localhost:3000
```

## Autor

Desenvolvido por **Guilherme Klinke**
