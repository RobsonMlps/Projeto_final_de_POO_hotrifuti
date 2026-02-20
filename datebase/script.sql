CREATE TABLE CATEGORIA (
    ID_Categoria SERIAL PRIMARY KEY,
    Nome VARCHAR(100) NOT NULL,
    Descricao TEXT
);

CREATE TABLE CLIENTE (
    ID_Cliente SERIAL PRIMARY KEY,
    CPF VARCHAR(14) NOT NULL,
    Nome VARCHAR(100) NOT NULL
);

CREATE TABLE PRODUTO (
    ID_Produto SERIAL PRIMARY KEY,
    ID_Categoria INT,
    Nome VARCHAR(100) NOT NULL,
    Descricao TEXT,
    Peso_KG DECIMAL(10,2), -- Adicionei (10,2) para limitar casas decimais
    FOREIGN KEY (ID_Categoria) REFERENCES CATEGORIA(ID_Categoria)
);

CREATE TABLE VENDA (
    ID_Venda SERIAL PRIMARY KEY,
    ID_Cliente INT,
	ID_Produto INT,
    Quantidade_Vendida INT NOT NULL,
    Preco_Unidade DECIMAL(10,2) NOT NULL,
    Data_Hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Mudado de DATETIME para TIMESTAMP
    FOREIGN KEY (ID_Cliente) REFERENCES CLIENTE(ID_Cliente),
	FOREIGN KEY (ID_Produto) REFERENCES PRODUTO(ID_Produto)
);

INSERT INTO CATEGORIA (Nome, Descricao) VALUES 
('Frutas', 'Frutas frescas nacionais e importadas'),
('Verduras', 'Folhas, alfaces e ervas frescas'),
('Legumes', 'Raízes, tubérculos e frutos não doces'),
('Grãos', 'Feijão, arroz, milho e cereais a granel'),
('Temperos', 'Condimentos secos e especiarias para cozinha');

INSERT INTO CLIENTE (CPF, Nome) VALUES 
('123.456.789-01', 'João Silva'),
('234.567.890-12', 'Maria Oliveira'),
('345.678.901-23', 'Carlos Eduardo Santos'),
('456.789.012-34', 'Ana Paula Souza'),
('567.890.123-45', 'Lucas Ferreira');