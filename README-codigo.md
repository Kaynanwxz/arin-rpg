gi# Arin RPG

Backend do projeto **Arin RPG**, construído com **Spring Boot** e **Java**, utilizando **JPA/Hibernate** para persistência e **Maven** como gerenciador de build.

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA / Hibernate
- Lombok
- Maven
- dnsjava (validação de domínio de e-mail)

## Pré-requisitos

- JDK instalado (verifique a versão exigida no `pom.xml`, tag `<java.version>`)
- Maven instalado (ou use o wrapper `./mvnw`, se presente no projeto)
- Um banco de dados configurado (ex: PostgreSQL, MySQL)

## Como rodar o projeto

Clone o repositório e, na raiz do projeto, execute:

```bash
mvn spring-boot:run
```

Ou, se o projeto usa o wrapper do Maven:

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

## Estrutura do projeto

```
src/main/java/arin_rpg/
├── component/
│   ├── TokenComponent.java     # Extrai/valida o usuário a partir do token JWT
│   └── ValidatorComponent.java # Validações agregadas de um User (senha, email, cpf, idade)
├── configuration/
│   └── SecurityConfig.java     # Configuração de segurança (ex: PasswordEncoder)
├── controller/
│   └── UserController.java     # Endpoints REST de usuário
├── model/
│   ├── User.java                # Entidade JPA do usuário
│   └── UserRequest.java         # DTO usado no login
├── repository/
│   └── UserRepository.java      # Acesso a dados de User (Spring Data JPA)
├── service/
│   ├── JwtService.java          # Geração de tokens JWT
│   └── UserService.java         # Regras de negócio de usuário
└── utils/
    ├── PasswordValidator.java # Validação de senha (regras de força)
    ├── EmailValidator.java    # Validação de existência de domínio (DNS/MX)
    ├── CpfValidator.java      # Validação de CPF (algoritmo dos dígitos verificadores)
    └── BirthValidator.java    # Validação de maioridade (18+)
```

## Endpoints da API

Base path: `/user`

| Método | Rota | Descrição | Corpo / Header |
|---|---|---|---|
| `GET` | `/user` | Lista todos os usuários | — |
| `POST` | `/user` | Cria um novo usuário | Body: `User` |
| `GET` | `/user/login` | Realiza login e retorna o token JWT | Body: `UserRequest` |
| `GET` | `/user/me` | Retorna o usuário autenticado a partir do token | Header: `Authorization` |

## Classes

### `User`

Entidade JPA mapeada para a tabela `user_table`.

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | Gerado automaticamente (`IDENTITY`) |
| `userName` | `String` | Nome de usuário |
| `email` | `String` | E-mail do usuário |
| `password` | `String` | Senha do usuário |
| `dateOfBirth` | `LocalDate` | Data de nascimento |
| `createAt` | `LocalDateTime` | Preenchido automaticamente na criação do objeto, com a data/hora atual |

```java
User user = new User();
user.setUserName("arin");
user.setEmail("arin@teste.com");
user.setPassword("Senha@123");
user.setDateOfBirth(LocalDate.of(2000, 1, 1));
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
- `Login(UserRequest userRequest)` — busca o usuário pelo email, verifica a senha e gera um token JWT (via `JwtService`).
- `GetMe(String authorization)` — retorna o usuário autenticado a partir do token (via `TokenComponent`).

```java
User novoUsuario = userService.CreateUser(user);
String token = userService.Login(userRequest);
```

> Depende de `UserRepository`, `JwtService`, `SecurityConfig`, `TokenComponent` e `ValidatorComponent`, injetados via `@RequiredArgsConstructor`.

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

## Contribuindo

1. Crie uma branch a partir da `main`/`develop`
2. Implemente sua alteração
3. Abra um Pull Request
