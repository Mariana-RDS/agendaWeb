
--CREATE DATABASE agenda_web;

-- Criação da tabela Usuario
CREATE TABLE Usuario(
	id SERIAL PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	username VARCHAR(45) UNIQUE NOT NULL,
	password VARCHAR(64),
	telefone_num VARCHAR(15),
	rua VARCHAR(100),
	cidade VARCHAR(50),
	estado VARCHAR(50)

);

-- Criação da tabela Contato
CREATE TABLE Contato(
	id SERIAL PRIMARY KEY,
	nome VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL,
	rua VARCHAR(100),
	cidade VARCHAR(50),
	estado VARCHAR(50),

	id_usuario INT,
	
	FOREIGN KEY (id_usuario) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- Criação da tabela Telefone
CREATE TABLE Telefone(
	id SERIAL PRIMARY KEY,
	numero VARCHAR(15) NOT NULL,

	id_contato INT,

	FOREIGN KEY (id_contato) REFERENCES Contato(id) ON DELETE CASCADE
);

	