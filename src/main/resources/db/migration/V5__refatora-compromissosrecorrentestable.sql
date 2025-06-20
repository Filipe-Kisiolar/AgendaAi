-- 1. Adiciona os novos campos à tabela compromissos_recorrentes
ALTER TABLE compromissos_recorrentes
ADD COLUMN intervalo INTEGER,
ADD COLUMN modo_de_recorrencia VARCHAR(50),
ADD COLUMN ordenamento_da_semana_no_mes VARCHAR(50),
ADD COLUMN apenas_dias_uteis BOOLEAN;

-- 2. Cria a nova tabela de horários por dia (agora com id e FK correta)
CREATE TABLE horarios_por_dia_compromissos_recorrentes (
    id BIGSERIAL PRIMARY KEY,
    dia_de_inicio VARCHAR(20),
    hora_de_inicio TIME,
    dia_de_fim VARCHAR(20),
    hora_de_fim TIME,
    inicio_data_especifica_do_ano TIMESTAMP,
    fim_data_especifica_do_ano TIMESTAMP,
    inicio_dia_especifico_do_mes INTEGER,
    fim_dia_especifico_do_mes INTEGER,
    compromisso_recorrente_id BIGINT,
    CONSTRAINT fk_compromisso_recorrente
        FOREIGN KEY (compromisso_recorrente_id)
        REFERENCES compromissos_recorrentes(id)
        ON DELETE CASCADE
);

-- 3. Migra os dados antigos da estrutura baseada em dias da semana
INSERT INTO horarios_por_dia_Compromissos_Recorrentes (
    dia_de_inicio,
    hora_de_inicio,
    dia_de_fim,
    hora_de_fim,
    compromisso_recorrente_id
)
SELECT
    crds.dias_da_semana,
    cr.hora_de_inicio,
    crds.dias_da_semana,
    cr.hora_de_final,
    crds.compromissos_recorrentes_id
FROM compromissos_recorrentes_dias_da_semana crds
JOIN compromissos_recorrentes cr ON cr.id = crds.compromissos_recorrentes_id;

-- 4. Renomeia colunas antigas como backup
ALTER TABLE compromissos_recorrentes
RENAME COLUMN hora_de_inicio TO hora_de_inicio_antiga;
ALTER TABLE compromissos_recorrentes
RENAME COLUMN hora_de_final TO hora_de_final_antiga;

-- 5. Permite null nos novos campos (para manter compatibilidade)
ALTER TABLE compromissos_recorrentes
ALTER COLUMN intervalo DROP NOT NULL,
ALTER COLUMN modo_de_recorrencia DROP NOT NULL,
ALTER COLUMN ordenamento_da_semana_no_mes DROP NOT NULL,
ALTER COLUMN apenas_dias_uteis DROP NOT NULL;
