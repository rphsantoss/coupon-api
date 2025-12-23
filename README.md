# Coupon API

API REST para gerenciamento de cupons com normalização de código, validações e funcionalidade de soft delete.

## Funcionalidades

- Criar cupons com normalização automática de código
- Buscar cupons por ID ou código
- Implementação de soft delete
- Validação de regras de negócio
- Banco de dados H2 em memória
- Testes de integração completos

## Tecnologias

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Maven
- WebTestClient

## Como Executar

### Instalação

1. Clone o repositório
```bash
git clone https://github.com/seuusuario/coupon-api.git
cd coupon-api
```

2. Compile o projeto
```bash
mvn clean install
```

3. Execute a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

## Endpoints da API

### Criar Cupom
```http
POST /api/coupons
Content-Type: application/json

{
  "code": "SAVE@10",
  "description": "Cupom de 10% de desconto",
  "discountValue": 10.00,
  "expirationDate": "2025-12-31",
  "published": true
}
```

**Resposta:** `201 Created`
```json
{
  "id": 1,
  "code": "SAVE10",
  "description": "Cupom de 10% de desconto",
  "discountValue": 10.00,
  "expirationDate": "2025-12-31",
  "published": true,
  "deleted": false
}
```

### Buscar Cupom por ID
```http
GET /api/coupons/{id}
```

**Resposta:** `200 OK`

### Buscar Cupom por Código
```http
GET /api/coupons/code/{code}
```

**Resposta:** `200 OK`

### Deletar Cupom (Soft Delete)
```http
DELETE /api/coupons/{id}
```

**Resposta:** `204 No Content`

### Buscar Cupom por Código
```http
GET /api/coupons/code/{code}
```

**Exemplo de requisição:**
```http
GET /api/coupons/code/SAVE10
```

**Resposta:** `200 OK`
```json
{
  "id": 1,
  "code": "SAVE10",
  "description": "Cupom de 10% de desconto",
  "discountValue": 10.00,
  "expirationDate": "2025-12-31",
  "published": true,
  "deleted": false
}
```

## Regras de Negócio

### Normalização de Código
- Aceita códigos alfanuméricos com caracteres especiais
- Remove automaticamente caracteres especiais
- Garante exatamente 6 caracteres
- Converte para maiúsculas

**Exemplo:** `"SAVE@10"` → `"SAVE10"`

### Validações
- Código deve ter pelo menos 6 caracteres alfanuméricos
- Data de expiração não pode estar no passado
- Valor de desconto mínimo: 0,50
- Códigos duplicados não são permitidos
- Não é possível deletar cupons já deletados

### Soft Delete
- Cupons deletados são marcados com `deleted = true`
- Cupons deletados não podem ser recuperados
- Código pode ser reutilizado após soft delete

## Executando os Testes

Executar todos os testes:
```bash
mvn test
```

Executar classe de teste específica:
```bash
mvn test -Dtest=CouponServiceTest
mvn test -Dtest=CouponControllerTest
```

### Cobertura de Testes
- Testes de integração da camada de serviço
- Testes de integração da camada de controller
- Testes de validação de regras de negócio
- Testes de comportamento do soft delete

## Banco de Dados

A aplicação utiliza banco de dados H2 em memória para desenvolvimento e testes.

### Console H2

Acesse o console H2 em: `http://localhost:8080/h2-console`

**Configurações de conexão:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(deixe em branco)*

## Estrutura do Projeto
```
src/
├── main/
│   ├── java/com/challenge/coupon_api/
│   │   ├── controller/
│   │   │   └── CouponController.java
│   │   ├── dto/
│   │   │   ├── CouponRequestDTO.java
│   │   │   └── CouponResponseDTO.java
│   │   ├── model/
│   │   │   └── Coupon.java
│   │   ├── repository/
│   │   │   └── CouponRepository.java
│   │   └── service/
│   │       └── CouponService.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/challenge/coupon_api/
        ├── controller/
        │   └── CouponControllerTest.java
        └── service/
            └── CouponServiceTest.java
```

## Autor

[@rphsantoss](https://github.com/rphsantoss)
