DELETE FROM usuario_funcao;
DELETE FROM funcao_funcionalidade;
DELETE FROM funcionalidade;
DELETE FROM funcao;

ALTER TABLE funcao ALTER COLUMN id RESTART WITH 1;
ALTER TABLE funcionalidade ALTER COLUMN id RESTART WITH 1;

INSERT INTO funcao (nome) VALUES
    ('ADMIN'),
    ('ESTOQUISTA'),
    ('AUDITOR');

INSERT INTO funcionalidade (funcionalidade) VALUES
    ('/excluir-bem'),
    ('/cadastrar-bem'),
    ('/listar-bem'),
    ('/editar-bem');

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (2, 2),
    (2, 3);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (3, 3);