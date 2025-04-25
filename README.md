# 🛍️ Sistema de E-commerce com Spring Boot e MySQL

Este projeto é uma aplicação web de e-commerce desenvolvida com **Spring Boot**, **Spring MVC**, **Thymeleaf** e **MySQL**. Ele implementa funcionalidades para clientes (cadastro, login, listagem de produtos, gerenciamento de carrinho e finalização de compras) e lojistas (login, listagem e cadastro de produtos). O carrinho é gerenciado via sessões HTTP, e os dados são armazenados em um banco MySQL usando JDBC puro.

## Desenvolvedores

- **Guilherme Felipe**
- **Mateo Delgado**

## Visão Geral

A aplicação atende aos seguintes casos de uso:
- **Clientes**:
  - Cadastro com nome, email e senha.
  - Login com email e senha.
  - Visualização de produtos disponíveis.
  - Adição e remoção de itens no carrinho.
  - Finalização de compras com atualização de estoque.
- **Lojistas**:
  - Login com email e senha.
  - Visualização de produtos cadastrados.
  - Cadastro de novos produtos (nome, descrição, preço e estoque).
- **Sessão**: O carrinho é mantido em uma sessão HTTP.

## Pré-requisitos

- **Java 21+**: JDK instalado ([Oracle JDK](https://www.oracle.com/java/) ou [OpenJDK](https://adoptium.net/)).
- **Maven**: Gerenciador de dependências ([Maven](https://maven.apache.org/download.cgi)).
- **MySQL**: Servidor MySQL instalado ([MySQL Community Server](https://dev.mysql.com/downloads/)).
- **IDE**: IntelliJ IDEA Community, VS Code com extensões Java ou outra IDE compatível.
- **MySQL Workbench** (opcional): Para gerenciar o banco de dados ([MySQL Workbench](https://dev.mysql.com/downloads/workbench/)).

## Instalação

Siga os passos abaixo para configurar e executar a aplicação localmente.

### 1. Configurar o Banco MySQL
1. Certifique-se de que o MySQL está instalado e rodando.
2. Abra o MySQL Workbench ou use o terminal MySQL:
   ```sql
   CREATE DATABASE meuecommerce;
   CREATE USER 'admin2025'@'localhost' IDENTIFIED BY 'admin2025';
   GRANT ALL PRIVILEGES ON meuecommerce.* TO 'admin2025'@'localhost';
   FLUSH PRIVILEGES;
   ```
3. As credenciais `admin2025` e senha `admin2025` são usadas por padrão. Você pode alterá-las no `application.properties`.

### 2. Clonar o Repositório
1. Clone o repositório para sua máquina local:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd meuecommerce
   ```

### 3. Configurar o Projeto
1. Abra o projeto na sua IDE (IntelliJ IDEA, VS Code, etc.).
2. Verifique o arquivo `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/meuecommerce?useSSL=false&serverTimezone=UTC
   spring.datasource.username=admin2025
   spring.datasource.password=admin2025
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```
   - Se alterou o usuário ou senha no passo 1, atualize `spring.datasource.username` e `spring.datasource.password`.
3. O arquivo `data.sql` (em `src/main/resources`) cria as tabelas `cliente`, `lojista`, e `produto` e insere dados iniciais automaticamente ao iniciar a aplicação.

### 4. Executar a Aplicação
1. Na IDE, execute a classe `MeuecommerceApplication.java`.
2. Ou, no terminal, na pasta do projeto:
   ```bash
   mvn spring-boot:run
   ```
3. A aplicação estará disponível em `http://localhost:8080/login`.

## Uso

### Acessar a Aplicação
- Abra o navegador e acesse `http://localhost:8080/login`.
- **Credenciais de Teste** (definidas em `data.sql`):
  - **Cliente**:
    - Email: `joao.pedro@example.com`, Senha: `12345jaum`
    - Email: `amara.silva@example.com`, Senha: `amara82`
    - Email: `maria.pereira@terra.com.br`, Senha: `145aektm`
  - **Lojista**:
    - Email: `tanirocr@email.com`, Senha: `123456abc`
    - Email: `lore_sil@yahoo.com.br`, Senha: `12uhuuu@`

### Funcionalidades
- **Clientes**:
  - Cadastre-se em `/cadastro`.
  - Faça login em `/login`.
  - Liste produtos em `/produtos`.
  - Adicione produtos ao carrinho e visualize em `/carrinho`.
  - Finalize a compra, atualizando o estoque.
- **Lojistas**:
  - Faça login em `/login`.
  - Liste produtos em `/lojista/produtos`.
  - Cadastre novos produtos em `/lojista/cadastro`.
- **Logout**: Disponível em todas as páginas.

### Verificar o Banco
- Use o MySQL Workbench ou terminal para consultar as tabelas:
  ```sql
  USE meuecommerce;
  SELECT * FROM cliente;
  SELECT * FROM lojista;
  SELECT * FROM produto;
  ```

## Estrutura do Projeto

```
meuecommerce/
├── src/
│   ├── main/
│   │   ├── java/com/meuecommerce/
│   │   │   ├── controller/                    # Controladores (AuthController, CarrinhoController, etc.)
│   │   │   ├── dao/                           # Classes DAO para acesso ao banco via JDBC
│   │   │   ├── model/                         # Modelos (Cliente, Lojista, Produto, Carrinho, etc.)
│   │   │   ├── util/                          # Utilitários (DatabaseConnection)
│   │   │   └── MeuecommerceApplication.java   # Classe principal
│   │   ├── resources/
│   │   │   ├── templates/                     # Templates Thymeleaf (login.html, lista-produtos.html, etc.)
│   │   │   ├── application.properties         # Configurações do projeto
│   │   │   └── data.sql                       # Schema e dados iniciais para o banco
│   └── test/                                  # Testes (não implementados neste projeto)
├── pom.xml                                    # Dependências Maven
└── README.md                                  # Documentação
```

## Tecnologias Utilizadas
- **Spring Boot 3.4.4**: Framework principal.
- **JDBC**: Persistência de dados com MySQL.
- **MySQL**: Banco de dados relacional.
- **Thymeleaf**: Renderização de páginas HTML.
- **Maven**: Gerenciamento de dependências.
- **Java 21**: Linguagem utilizada.
