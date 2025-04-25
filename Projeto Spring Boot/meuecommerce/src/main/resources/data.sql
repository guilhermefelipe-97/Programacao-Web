-- Tabela para clientes
CREATE TABLE IF NOT EXISTS cliente (
                                       email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
    );

-- Tabela para lojistas
CREATE TABLE IF NOT EXISTS lojista (
                                       email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
    );

-- Tabela para produtos
CREATE TABLE IF NOT EXISTS produto (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL
    );

-- Dados iniciais para clientes
INSERT IGNORE INTO cliente (email, nome, senha) VALUES ('joao.pedro@example.com', 'João Pedro', '12345jaum');
INSERT IGNORE INTO cliente (email, nome, senha) VALUES ('amara.silva@example.com', 'Amara Silva', 'amara82');
INSERT IGNORE INTO cliente (email, nome, senha) VALUES ('maria.pereira@terra.com.br', 'Maria Pereira', '145aektm');

-- Dados iniciais para lojistas
INSERT IGNORE INTO lojista (email, nome, senha) VALUES ('tanirocr@email.com', 'Taniro Rodrigues', '123456abc');
INSERT IGNORE INTO lojista (email, nome, senha) VALUES ('lore_sil@yahoo.com.br', 'Lorena Silva', '12uhuuu@');

-- Dados iniciais para produtos
INSERT IGNORE INTO produto (nome, descricao, preco, estoque) VALUES ('Mesa', 'Uma mesa de computador.', 500.00, 10);
INSERT IGNORE INTO produto (nome, descricao, preco, estoque) VALUES ('Lapis', 'Lapis 82 grafite.', 2.00, 50);
INSERT IGNORE INTO produto (nome, descricao, preco, estoque) VALUES ('Computador', 'Computador i5 16Gb de RAM.', 1500.00, 2);