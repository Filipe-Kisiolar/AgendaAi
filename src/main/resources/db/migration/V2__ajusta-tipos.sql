-- Alterar tipo da coluna 'id' para BIGINT com autoincremento (BIGSERIAL)

-- Tabela compromissos_recorrentes
ALTER TABLE compromissos_recorrentes
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN id SET DEFAULT nextval('compromissos_recorrentes_id_seq'::regclass);

-- Tabela compromissos
ALTER TABLE compromissos
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN id SET DEFAULT nextval('compromissos_id_seq'::regclass),
    ALTER COLUMN compromissorecorrente_id TYPE BIGINT;

-- Tabela compromissos_recorrentes_dias_da_semana
ALTER TABLE compromissos_recorrentes_dias_da_semana
    ALTER COLUMN compromissos_recorrentes_id TYPE BIGINT;

-- Recriar constraints com tipos corretos (opcional se o PostgreSQL não reclamar)
-- (Se der erro, primeiro DROP CONSTRAINT, depois ADD CONSTRAINT)

-- Apenas para garantir integridade
ALTER TABLE compromissos
    DROP CONSTRAINT IF EXISTS fk_compromissorecorrente,
    ADD CONSTRAINT fk_compromissorecorrente
        FOREIGN KEY (compromissorecorrente_id) REFERENCES compromissos_recorrentes(id);

ALTER TABLE compromissos_recorrentes_dias_da_semana
    DROP CONSTRAINT IF EXISTS fk_dias_compromisso,
    ADD CONSTRAINT fk_dias_compromisso
        FOREIGN KEY (compromissos_recorrentes_id) REFERENCES compromissos_recorrentes(id) ON DELETE CASCADE;
