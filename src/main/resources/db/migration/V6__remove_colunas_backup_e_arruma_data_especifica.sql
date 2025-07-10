
-- 1. Alterar os campos de MonthDay para VARCHAR
ALTER TABLE horarios_por_dia_compromissos_recorrentes
    ALTER COLUMN inicio_data_especifica_do_ano TYPE VARCHAR(10),
    ALTER COLUMN fim_data_especifica_do_ano TYPE VARCHAR(10);

-- 2. Remover colunas antigas de backup que não são mais utilizadas
ALTER TABLE compromissos_recorrentes
    DROP COLUMN IF EXISTS hora_de_inicio_antiga,
    DROP COLUMN IF EXISTS hora_de_final_antiga,
    DROP COLUMN IF EXISTS dia_antigo;
