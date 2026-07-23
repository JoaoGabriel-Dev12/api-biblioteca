# API de Biblioteca
Esta API REST consiste na gestão de uma biblioteca (clientes, livros, empréstimos, devoluções), voltado para o uso interno,
como secretário/bibliotecário

## Funcionalidades
- CRUD de Clientes e Livros
- Controle de empréstimos e devoluções
- Notificação para o email do cliente, quando for cadastrado, e quando realizar um empréstimo ou devolução

## Tecnologias
Ferramentas utilizadas:
- Java 21
- Spring Boot 4
- PostgreSQL
- Spring JPA
- Redis
- Swagger
- Spring Mail

## Como rodar o projeto
### Requisitos
Java, Postgres e Redis instalados na máquina local
### Clone o repositório
```bash
git clone https://github.com/JoaoGabriel-Dev12/api-biblioteca.git
```
### Configurar as variáveis de ambiente
Crie um arquivo .env na pasta raiz do projeto, e insira as seguintes variáveis
```bash
DATABASE_USER=<seu-usuario>
DATABASE_PASSWORD=<sua-senha>

MAIL_USERNAME=<seu-email>
MAIL_PASSWORD=<sua-senha-de-app>
```
No application.properties importe o .env e configure as variáveis
```bash
# Importar .env
spring.config.import=optional:file:.env[.properties]

# Configurar variáveis
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```
### Rodar o projeto localmente
No terminal digite
```bash
mvn spring-boot:run
```
## Endpoints
Com a aplicação rodando localmente, acesse: http://localhost:8080/swagger-ui.html
<br>
Com este link é possível testar os endpoints
## Sobre o projeto
Projeto pessoal de estudo, desenvolvido para praticar framework Spring Boot, arquitetura em camadas,
tratamento de exceções, cache e integração com serviços externos.
## Próximos passos
- [ ] Validação com Bean Validation
- [ ] Autenticação com Spring Security
- [ ] Migrações de banco com Flyway
