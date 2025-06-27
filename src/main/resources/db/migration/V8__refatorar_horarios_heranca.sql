-- 1. Remove a tabela antiga de dias da semana (caso ainda exista)
DROP TABLE IF EXISTS compromissos_recorrentes_dias_da_semana;

-- 2. Renomeia a tabela base de horários para refletir o modelo abstrato
ALTER TABLE horarios_por_dia_compromissos_recorrentes
RENAME TO horarios_por_dia_compromissos_recorrentes_model;

-- 3. Adiciona coluna dtype necessária para a herança JOINED
ALTER TABLE horarios_por_dia_compromissos_recorrentes_model
ADD COLUMN dtype VARCHAR(50);

-- 4. Remove o campo ordenamento_da_semana_no_mes da tabela de compromissos recorrentes
ALTER TABLE compromissos_recorrentes
DROP COLUMN IF EXISTS ordenamento_da_semana_no_mes;

-- 5. Cria tabela para horários com frequência diária
CREATE TABLE horarios_frequencia_diaria (
    id BIGINT PRIMARY KEY,
    hora_de_inicio TIME NOT NULL,
    hora_de_fim TIME NOT NULL,
    CONSTRAINT fk_diaria_base
        FOREIGN KEY (id)
        REFERENCES horarios_por_dia_compromissos_recorrentes_model(id)
        ON DELETE CASCADE
);

-- 6. Cria tabela para horários com frequência semanal
CREATE TABLE horarios_frequencia_semanal (
    id BIGINT PRIMARY KEY,
    dia_da_semana_inicio VARCHAR(20) NOT NULL,
    dia_da_semana_fim VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    CONSTRAINT fk_semanal_base
        FOREIGN KEY (id)
        REFERENCES horarios_por_dia_compromissos_recorrentes_model(id)
        ON DELETE CASCADE
);

-- 7. Cria tabela para horários padrão relativo mensal
CREATE TABLE horarios_padrao_relativo_mensal (
    id BIGINT PRIMARY KEY,
    dia_da_semana_inicio VARCHAR(20) NOT NULL,
    dia_da_semana_fim VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    ordenamento_da_semana_no_mes VARCHAR(30) NOT NULL,
    CONSTRAINT fk_relativo_base
        FOREIGN KEY (id)
        REFERENCES horarios_por_dia_compromissos_recorrentes_model(id)
        ON DELETE CASCADE
);

-- 8. Cria tabela para horários dia específico do mês
CREATE TABLE horarios_dia_especifico_mensal (
    id BIGINT PRIMARY KEY,
    inicio_dia_especifico_do_mes INTEGER NOT NULL,
    fim_dia_especifico_do_mes INTEGER NOT NULL,
    hora_de_inicio TIME NOT NULL,
    hora_de_fim TIME NOT NULL,
    CONSTRAINT fk_mensal_especifico_base
        FOREIGN KEY (id)
        REFERENCES horarios_por_dia_compromissos_recorrentes_model(id)
        ON DELETE CASCADE
);

-- 9. Cria tabela para horários data específica anual
CREATE TABLE horarios_data_especifica_anual (
    id BIGINT PRIMARY KEY,
    inicio_data_especifica_do_ano VARCHAR(10) NOT NULL, -- formato MM-DD
    fim_data_especifica_do_ano VARCHAR(10) NOT NULL,
    hora_de_inicio TIME NOT NULL,
    hora_de_fim TIME NOT NULL,
    CONSTRAINT fk_anual_especifico_base
        FOREIGN KEY (id)
        REFERENCES horarios_por_dia_compromissos_recorrentes_model(id)
        ON DELETE CASCADE
);

-- 10. Migra os dados antigos (assumindo que eram todos semanais) para a nova tabela de frequência semanal
INSERT INTO horarios_frequencia_semanal (
    id,
    dia_da_semana_inicio,
    dia_da_semana_fim,
    hora_inicio,
    hora_fim
)
SELECT
    id,
    dia_da_semana_de_inicio,
    dia_da_semana_de_fim,
    hora_de_inicio,
    hora_de_fim
FROM horarios_por_dia_compromissos_recorrentes_model
WHERE dia_de_inicio IS NOT NULL AND dia_de_fim IS NOT NULL;
