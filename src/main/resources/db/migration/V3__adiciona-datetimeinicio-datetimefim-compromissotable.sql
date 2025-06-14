-- Renomear colunas antigas do tipo TIME
ALTER TABLE compromissos
RENAME COLUMN inicio TO hora_inicial;

ALTER TABLE compromissos
RENAME COLUMN final TO hora_final;

-- Adicionar novas colunas do tipo TIMESTAMP
ALTER TABLE compromissos
ADD COLUMN inicio TIMESTAMP,
ADD COLUMN fim TIMESTAMP;

-- Preencher as novas colunas com base nos dados existentes
UPDATE compromissos
SET
    inicio = dia + hora_inicial,
    fim = CASE
             WHEN hora_final < hora_inicial THEN dia + hora_final + INTERVAL '1 day'
             ELSE dia + hora_final
         END;
