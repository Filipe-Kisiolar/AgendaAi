
-- Atualiza o campo modo_de_recorrencia para 'FREQUENCIA_SEMANAL'
UPDATE compromissos_recorrentes
SET modo_de_recorrencia = 'FREQUENCIA_SEMANAL'
WHERE modo_de_recorrencia IS NULL;
