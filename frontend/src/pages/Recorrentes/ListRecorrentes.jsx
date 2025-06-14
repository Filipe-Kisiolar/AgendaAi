import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

export default function ListRecorrentes() {
  const [recorrentes, setRecorrentes] = useState([])

  useEffect(() => {
    fetch('/compromissosrecorrentes/listarcompromissos')
      .then(res => res.json())
      .then(data => setRecorrentes(data.listaCompromissosRecorrentes || []))
  }, [])

  return (
    <div>
      <h2>Compromissos Recorrentes</h2>
      <Link to="/recorrentes/novo">Novo Compromisso</Link>
      <ul>
        {recorrentes.map(c => (
          <li key={c.id}>{c.nome} - {c.diaDaSemana}</li>
        ))}
      </ul>
    </div>
  )
}
