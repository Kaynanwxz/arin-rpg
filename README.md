# Arin RPG

Backend do projeto **Arin RPG**, construído com **Spring Boot** e **Java**, utilizando **JPA/Hibernate** para persistência, **Spring Security** para autenticação via JWT e **Maven** como gerenciador de build.

## Tecnologias

- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security
- jjwt (geração e validação de tokens JWT)
- Lombok
- Maven
- H2 Database (banco em memória, disponível em runtime)
- dnsjava (validação de domínio de e-mail)

## Pré-requisitos

- JDK 21 instalado
- Maven instalado (ou use o wrapper `./mvnw`, presente no projeto)
- Um banco de dados configurado (ex: PostgreSQL, MySQL) — ou use o H2 em memória para rodar localmente sem configurar nada

## Como rodar o projeto

Clone o repositório e, na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

A aplicação vai subir na porta padrão do Spring Boot (`8080`, salvo configuração diferente em `application.properties`).

### Configuração do banco de dados

Antes de rodar, configure a conexão com o banco em `src/main/resources/application.properties`, por exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/arin_rpg
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

### Configuração do JWT

O segredo usado para assinar os tokens JWT deve vir de uma variável de ambiente, nunca do código-fonte:

```properties
jwt.secret=${JWT_SECRET}
```

## Estrutura do projeto

```
src/main/java/arin_rpg/
├── component/
│   ├── TokenComponent.java      # Extrai e valida o usuário autenticado a partir do header Authorization
│   └── ValidatorComponent.java  # Agrega as validações de um User no cadastro (email único, idade, senha, domínio)
├── configuration/
│   └── SecurityConfig.java      # PasswordEncoder (BCrypt) e cadeia de filtros de segurança
├── controller/
│   └── UserController.java      # Endpoints REST de usuário
├── model/
│   ├── User.java                 # Entidade JPA do usuário
│   └── UserRequest.java          # DTO usado no login (email + senha)
├── repository/
│   └── UserRepository.java       # Acesso a dados de User (Spring Data JPA)
├── service/
│   ├── JwtService.java           # Geração e leitura de tokens JWT
│   └── UserService.java          # Regras de negócio de usuário
└── utils/
    ├── PasswordValidator.java  # Validação de senha (regras de força)
    ├── EmailValidator.java     # Validação de existência de domínio (DNS/MX)
    ├── CpfValidator.java       # Validação de CPF (algoritmo dos dígitos verificadores)
    └── BirthValidator.java     # Validação de maioridade (18+)
```

## Endpoints da API

Base path: `/user`

| Método | Rota | Descrição | Corpo / Header |
|---|---|---|---|
| `GET` | `/user` | Lista todos os usuários | — |
| `POST` | `/user` | Cria um novo usuário | Body: `User` |
| `GET` | `/user/login` | Realiza login e retorna o token JWT | Body: `UserRequest` |
| `GET` | `/user/me` | Retorna o usuário autenticado a partir do token | Header: `Authorization: Bearer <token>` |

## Classes

### `User`

Entidade JPA mapeada para a tabela `user_table`.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Gerado automaticamente (`IDENTITY`) |
| `userName` | `String` | Nome de usuário |
| `email` | `String` | E-mail do usuário |
| `password` | `String` | Senha do usuário (armazenada com hash) |
| `dateOfBirth` | `LocalDate` | Data de nascimento |
| `createAt` | `LocalDateTime` | Preenchido automaticamente na criação do objeto |

```java
User user = new User();
user.setUserName("arin");
user.setEmail("arin@teste.com");
user.setPassword("Senha@123");
user.setDateOfBirth(LocalDate.of(2000, 1, 1));
```

### `UserRequest`

DTO usado no login, contendo apenas `email` e `password`.

```java
UserRequest request = new UserRequest();
request.setEmail("arin@teste.com");
request.setPassword("Senha@123");
```

### `UserController`

Controller REST responsável pelos endpoints de usuário (ver tabela de endpoints acima). Delega toda a lógica para `UserService`.

```java
@GetMapping()
public List<User> GetUsers() {
    return userService.getUser();
}
```

### `UserService`

Camada de serviço com as regras de negócio de usuário:

- `getUser()` — retorna todos os usuários cadastrados.
- `CreateUser(User user)` — valida o usuário (via `ValidatorComponent`), criptografa a senha (via `SecurityConfig.passwordEncoder()`) e salva no `UserRepository`.
- `Login(UserRequest userRequest)` — busca o usuário pelo email e gera um token JWT (via `JwtService`).
- `GetMe(String authorization)` — retorna o usuário autenticado a partir do token (via `TokenComponent`).

```java
User novoUsuario = userService.CreateUser(user);
String token = userService.Login(userRequest);
```

> Depende de `UserRepository`, `JwtService`, `SecurityConfig`, `TokenComponent` e `ValidatorComponent`, injetados via `@RequiredArgsConstructor`.

### `ValidatorComponent`

Centraliza as validações aplicadas a um `User` no momento do cadastro:

- E-mail ainda não cadastrado (`UserRepository.existsByEmail`)
- Maioridade (`BirthValidator`)
- Força da senha (`PasswordValidator`)
- Existência do domínio do e-mail (`EmailValidator`)

```java
validatorComponent.UserIsValid(user); // lança RuntimeException se algo for inválido
```

### `TokenComponent`

Extrai o usuário autenticado a partir do header `Authorization`, validando o formato `Bearer <token>` e decodificando o e-mail contido no JWT.

```java
User usuarioAutenticado = tokenComponent.getUserFromToken(authorizationHeader);
```

### `JwtService`

Responsável por gerar e ler os tokens JWT (expiração de 1 hora), usando o segredo configurado via `jwt.secret`.

```java
String token = jwtService.generateToken("arin@teste.com");
String email = jwtService.getUserFromToken(token);
```

### `SecurityConfig`

Define o `PasswordEncoder` (BCrypt) usado para criptografar senhas e a cadeia de filtros de segurança (`SecurityFilterChain`) da aplicação.

### `UserRepository`

Interface Spring Data JPA para acesso aos dados de `User`.

```java
Optional<User> user = userRepository.findByEmail("arin@teste.com");
boolean existe = userRepository.existsByEmail("arin@teste.com");
```

### `PasswordValidator`

Valida se uma senha atende aos requisitos mínimos de segurança: mínimo de 8 caracteres, pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial (`@$!%*?&.#_-`).

```java
boolean valida = PasswordValidator.isValid("Senha@123");
```

### `EmailValidator`

Verifica se o domínio de um e-mail possui registro MX válido (ou seja, se o domínio existe e está apto a receber e-mails). Faz uma consulta DNS real — requer conexão com a internet.

```java
boolean existe = EmailValidator.domainExists("usuario@gmail.com");
```

### `CpfValidator`

Valida um CPF (com ou sem formatação: aceita tanto `52998224725` quanto `529.982.247-25`), checando dígitos repetidos e os dígitos verificadores.

```java
boolean valido = CpfValidator.isValid("529.982.247-25");
```

### `BirthValidator`

Verifica se uma pessoa é maior de idade (18 anos ou mais) a partir da data de nascimento.

```java
boolean maiorDeIdade = BirthValidator.isValid(LocalDate.of(2000, 1, 1));
```

## Como rodar os testes

```bash
./mvnw test
```

## Contribuindo

1. Crie uma branch a partir da `main`/`develop`
2. Implemente sua alteração
3. Garanta que os testes passam (`./mvnw test`)
4. Abra um Pull Request
