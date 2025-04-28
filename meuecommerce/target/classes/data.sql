CREATE TABLE IF NOT EXISTS cliente(
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS lojista(
    email VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS produto
(   id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DOUBLE NOT NULL,
    estoque INT NOT NULL
);

INSERT INTO cliente (email, nome, senha)
VALUES ('joao.pedro@example.com', 'João Pedro', '12345jaum'),
       ('amara.silva@example.com', 'Amara Silva', 'amara82'),
       ('maria.pereira@terra.com.br', 'Maria Pereira', '145aektm');

INSERT INTO lojista (email, nome, senha)
VALUES ('tanirocr@email.com', 'Taniro Rodrigues', '123456abc'),
       ('lore_sil@yahoo.com.br', 'Lorena Silva', '12uhuuu@');

INSERT INTO produto (nome, descricao, preco, estoque)
VALUES ('Smartphone Galaxy S23', 'Smartphone com 128GB e câmera de 50MP', 3500.00, 10),
       ('Notebook Dell Inspiron', 'Notebook com 16GB RAM e SSD 512GB', 4500.00, 5),
       ('Fone de Ouvido JBL', 'Fone sem fio com cancelamento de ruído', 300.00, 20);