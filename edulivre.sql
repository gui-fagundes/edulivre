DROP DATABASE IF EXISTS edulivre;

CREATE DATABASE edulivre;

\c edulivre;

CREATE TABLE USUARIO(
    id serial primary key,
    nome varchar(50) not null,
    email varchar(150) unique not null,
    senha text not null,
    perfil text CHECK(perfil in ('aluno', 'professor', 'admin'))
);

CREATE TABLE CURSO(
    id serial primary key,
    titulo varchar(50) not null,
    descricao text not null,
    data_criacao timestamp not null default NOW(),
    avaliacao jsonb
);

CREATE TABLE MATRICULA(
    id serial primary key,
    usuario_id integer references usuario(id),
    curso_id integer references curso(id),
    data_matricula timestamp not null default NOW()
);

CREATE TABLE CONTEUDO(
    id serial primary key,
    curso_id integer references curso(id),
    titulo varchar(250) not null,
    descricao text not null,
    tipo text not null CHECK(tipo in ('video', 'pdf', 'imagem', 'audio', 'quiz', 'slide')),
    arquivo bytea
);