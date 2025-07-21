UPDATE compromissos
   SET usuario_id = 1
 WHERE usuario_id IS NULL;

 UPDATE compromissos_recorrentes
    SET usuario_id = 1
  WHERE usuario_id IS NULL;