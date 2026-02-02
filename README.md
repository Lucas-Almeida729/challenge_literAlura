# 📚 LiterAlura - Catálogo de Livros

O **LiterAlura** é um desafio de programação que consiste em desenvolver um catálogo de livros com interação textual via console. A aplicação consome dados da API **Gutendex**, processa informações em formato JSON e armazena os dados de interesse num banco de dados relacional.



## 🛠️ Tecnologias Utilizadas

* **Java 17**: Linguagem base do projeto.
* **Spring Boot 4.0.2**: Framework para desenvolvimento ágil de aplicações Java.
* **Spring Data JPA**: Para persistência de dados e consultas ao banco.
* **PostgreSQL**: Banco de dados relacional para armazenamento local.
* **Jackson (com.fasterxml.jackson)**: Para desserialização de objetos JSON da API.
* **HttpClient/HttpRequest**: Para realizar solicitações à API Gutendex.

## 📋 Funcionalidades Principais

O sistema oferece um menu interativo com as seguintes opções:

1. **Buscar livro pelo título**: Consulta a API Gutendex, mapeia o primeiro resultado e salva o livro e o seu autor no banco de dados (evitando duplicatas).
2. **Listar livros registrados**: Exibe todos os livros armazenados no banco de dados local.
3. **Listar autores registrados**: Lista todos os autores salvos, exibindo os seus anos de nascimento e falecimento.
4. **Listar autores vivos em um determinado ano**: Consulta o banco para encontrar autores que estavam vivos no ano informado pelo usuário.
5. **Listar livros em um determinado idioma**: Filtra os livros salvos por código de idioma (ex: `pt`, `en`, `fr`).

## 📁 Estrutura do Código

* **`model`**: Contém as Entidades JPA (`Livro`, `Autor`) e os Records para recepção do JSON (`DadosLivro`, `DadosAutor`).
* **`repository`**: Interfaces que estendem `JpaRepository` para gerenciar as consultas ao PostgreSQL.
* **`service`**: Contém a lógica de consumo da API (`ConsumoApi`) e o conversor de dados (`ConverteDados`).
* **`principal`**: Classe que orquestra a lógica de negócio e o menu do usuário.

## ⚙️ Configuração do Ambiente

### 1. Banco de Dados
Certifique-se de ter o PostgreSQL instalado e uma base de dados criada com o nome `literalura_db`.

### 2. Variáveis de Ambiente
O projeto utiliza a variável `${DB_USER}` para a usuário do banco de dados no arquivo `application.properties`. Configure-a no seu IDE ou sistema operacional.
O projeto utiliza a variável `${DB_PASSWORD}` para a senha do banco de dados no arquivo `application.properties`. Configure-a no seu IDE ou sistema operacional.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=postgres
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
