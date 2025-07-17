CREATE TABLE usuarios (
    id Serial PRIMARY KEY,
    nome VARCHAR(50),
    email VARCHAR(50),
    senha VARCHAR(50),
    tipo_de_permissao VARCHAR(20)
);

ALTER TABLE compromissos
ADD COLUMN usuario_id INTEGER,
ADD CONSTRAINT fk_usuario_compromissos FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;

ALTER TABLE compromissos_recorrentes
ADD COLUMN usuario_id INTEGER,
ADD CONSTRAINT fk_usuario_recorrentes FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE CASCADE;