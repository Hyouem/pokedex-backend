# RetroDex — Backend
**Interface visual:** [RetroDex — React e TypeScript](https://github.com/Hyouem/pokedex-frontend).


Projeto bônus de portfólio: Pokédex fullstack.

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring MVC e RestClient
- Jackson 3
- Maven
- JUnit e Mockito

## Endpoint

GET /api/pokemon/{nameOrId}

Exemplos:

- GET /api/pokemon/pikachu
- GET /api/pokemon/25

Resposta: id, name, imageUrl, types, heightMeters,
weightKg e abilities.

## Códigos HTTP

- 200: Pokémon encontrado.
- 400: parâmetro de busca inválido.
- 404: Pokémon inexistente.
- 502: falha na integração com a PokéAPI.

## Executar testes

```powershell
mvn clean verify
```

## Iniciar a API

```powershell
mvn spring-boot:run
```

## Testar a API

Em outro terminal:

```powershell
Invoke-RestMethod http://localhost:8080/api/pokemon/pikachu
Invoke-RestMethod http://localhost:8080/api/pokemon/25
```

## Configuração

- PORT: porta da aplicação (padrão 8080).
- POKEAPI_BASE_URL: endereço da API externa.

## Arquitetura

- controller: recebe requisições HTTP.
- service: valida a busca e transforma os dados.
- client: consulta a PokéAPI.
- dto: define os dados da resposta.
- exception: padroniza os erros HTTP.

## Próximas etapas

- Validar integração real com a PokéAPI.
- Criar frontend React + TypeScript.
- Construir o visual da RetroDex.
- Adicionar navegação, stats, favoritos e som.
- Publicar no GitHub e configurar deploy.

Dados fornecidos pela PokéAPI: https://pokeapi.co/