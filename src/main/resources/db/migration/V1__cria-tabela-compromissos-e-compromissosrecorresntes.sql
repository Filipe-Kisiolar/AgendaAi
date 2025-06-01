-- Criação da tabela compromissos_recorrentes
CREATE TABLE compromissos_recorrentes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    descricao TEXT,
    local VARCHAR(255),
    hora_de_inicio TIME,
    hora_de_final TIME,
    inicio_da_recorrencia DATE,
    fim_da_recorrencia DATE
);

-- Criação da tabela auxiliar para os dias da semana (Enum)
CREATE TABLE compromissos_recorrentes_dias_da_semana (
    compromissos_recorrentes_id INTEGER NOT NULL,
    dias_da_semana VARCHAR(20) NOT NULL,
    CONSTRAINT fk_dias_compromisso FOREIGN KEY (compromissos_recorrentes_id) REFERENCES compromissos_recorrentes(id) ON DELETE CASCADE
);

-- Criação da tabela compromissos
CREATE TABLE compromissos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    descricao TEXT,
    local VARCHAR(255),
    dia DATE,
    inicio TIME,
    final TIME,
    compromissorecorrente_id INTEGER,
    CONSTRAINT fk_compromissorecorrente FOREIGN KEY (compromissorecorrente_id) REFERENCES compromissos_recorrentes(id)
);