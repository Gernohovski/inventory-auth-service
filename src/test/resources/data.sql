DELETE FROM usuario_funcao;
DELETE FROM funcao;

ALTER TABLE funcao ALTER COLUMN id RESTART WITH 1;

INSERT INTO funcao (nome) VALUES
    ('ADMIN'),
    ('ESTOQUISTA'),
    ('AUDITOR');
