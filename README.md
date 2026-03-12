# Voll.med API

API REST de exemplo para cadastro e manutenção de médicos.

## Stack
- Java 17
- Spring Boot 3.5.x (Web, Validation, Data JPA)
- Flyway (migrações)
- MySQL (runtime) / H2 (test)
- Lombok

## Como rodar (Windows)
1. Suba um MySQL e crie o banco `vollmed_api`.
2. Ajuste as configurações em `src/main/resources/application.properties`.
3. Rode a aplicação:

```bat
mvnw.cmd spring-boot:run
```

A API sobe, por padrão, em `http://localhost:8080`.

## Banco de dados e migrações
- Migrações ficam em `src/main/resources/db/migration` e são executadas pelo Flyway no startup.
- Scripts atuais:
  - `V1__create-table-medicos.sql`
  - `V2__alter-table-medicos-add-collumn-telefone.sql`
  - `V3__alter-table-medicos-add-collumn-ativo.sql` (soft delete)

## Endpoints

### GET /hello
Retorna uma string de teste.

### POST /medicos
Cria um médico.
- Status: `201 Created`
- Header: `Location: /medicos/{id}`
- Body: `DadosDetalheMedico` (id, nome)

Exemplo:

```bash
curl -i http://localhost:8080/medicos \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Medico Teste\",\"email\":\"medico@teste.com\",\"telefone\":\"61999999999\",\"crm\":\"123456\",\"especialidade\":\"CARDIOLOGIA\",\"endereco\":{\"logradouro\":\"rua 1\",\"bairro\":\"bairro\",\"cep\":\"00000000\",\"cidade\":\"brasilia\",\"uf\":\"DF\",\"complemento\":\"apto\"}}"
```

Campos e validações principais (`DadosCadastroMedico`):
- `nome`: obrigatório
- `email`: obrigatório, formato de e-mail
- `crm`: obrigatório, 4 a 6 dígitos
- `telefone`: obrigatório
- `especialidade`: obrigatório (ORTOPEDIA, CARDIOLOGIA, GINECOLOGIA, DERMATOLOGIA)
- `endereco`: obrigatório
  - `cep`: 8 dígitos

### GET /medicos
Lista médicos ativos com paginação.
- Retorno: `Page<DadosListagemMedico>`
- Default: `size=10`, `sort=nome`

Query params suportados (Spring Data): `page`, `size`, `sort`.

### PUT /medicos
Atualiza dados do médico.
- Status: `200 OK`
- Body: `DadosDetalheMedico` (id, nome)

Payload (`DadosAtualizarMedico`):
- `id`: obrigatório
- `nome`, `telefone`, `endereco`: opcionais

### DELETE /medicos/{id}
Exclusão lógica (marca `ativo=false`).
- Status: `204 No Content`
- Body: vazio

## Erros e validação
- `400 Bad Request`: quando falha validação. Retorna uma lista no formato:

```json
[{"campo":"nome","mensagem":"..."}]
```

- `404 Not Found`: para `EntityNotFoundException` (ex.: buscar referência inexistente).

## Testes

```bat
mvnw.cmd test
```

## Observações do projeto
- A listagem (`GET /medicos`) traz apenas registros com `ativo=true`.
- Evite versionar credenciais em `application.properties`; prefira variáveis de ambiente/secret manager em ambientes reais.
