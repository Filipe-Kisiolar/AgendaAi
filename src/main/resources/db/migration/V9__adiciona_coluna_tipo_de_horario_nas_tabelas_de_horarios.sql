-- 1) adiciona a coluna
ALTER TABLE horarios_por_dia_compromissos_recorrentes_model
  ADD COLUMN tipo_horario VARCHAR(50);

-- 2) popula para cada subtipo
UPDATE horarios_por_dia_compromissos_recorrentes_model h
SET tipo_horario = 'SEMANAL'
WHERE EXISTS (
  SELECT 1 FROM horarios_frequencia_semanal s WHERE s.id = h.id
);

UPDATE horarios_por_dia_compromissos_recorrentes_model h
SET tipo_horario = 'DIARIA'
WHERE EXISTS (
  SELECT 1 FROM horarios_frequencia_diaria d WHERE d.id = h.id
);

UPDATE horarios_por_dia_compromissos_recorrentes_model h
SET tipo_horario = 'PADRAO_RELATIVO_MENSAL'
WHERE EXISTS (
  SELECT 1 FROM horarios_padrao_relativo_mensal r WHERE r.id = h.id
);

UPDATE horarios_por_dia_compromissos_recorrentes_model h
SET tipo_horario = 'DIA_ESPECIFICO_MENSAL'
WHERE EXISTS (
  SELECT 1 FROM horarios_dia_especifico_mensal m WHERE m.id = h.id
);

UPDATE horarios_por_dia_compromissos_recorrentes_model h
SET tipo_horario = 'DATA_ESPECIFICA_ANUAL'
WHERE EXISTS (
  SELECT 1 FROM horarios_data_especifica_anual a WHERE a.id = h.id
);
