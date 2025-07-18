DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS funcionalidade (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    funcionalidade VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS funcao (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    CONSTRAINT uk_funcao_nome UNIQUE (nome)
);

CREATE TABLE IF NOT EXISTS funcao_funcionalidade (
    funcao_id BIGINT NOT NULL,
    funcionalidade_id BIGINT NOT NULL,
    PRIMARY KEY (funcao_id, funcionalidade_id),
    CONSTRAINT fk_funcao_funcionalidade_funcao FOREIGN KEY (funcao_id) REFERENCES funcao (id) ON DELETE CASCADE,
    CONSTRAINT fk_funcao_funcionalidade_funcionalidade FOREIGN KEY (funcionalidade_id) REFERENCES funcionalidade (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN,
    data_criacao TIMESTAMP,
    data_alteracao TIMESTAMP,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS usuario_funcao (
    usuario_id BIGINT NOT NULL,
    funcao_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, funcao_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (funcao_id) REFERENCES funcao(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_funcao_nome ON funcao(nome);
CREATE INDEX IF NOT EXISTS idx_funcionalidade_nome ON funcionalidade(funcionalidade);
