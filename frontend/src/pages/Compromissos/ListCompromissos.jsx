import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

export default function ListCompromissos() {
  const [compromissos, setCompromissos] = useState([])

  useEffect(() => {
    fetch('/compromissos/listarcompromissos')
      .then(res => res.json())
      .then(data => setCompromissos(data.listaCompromissos || []))
  }, [])

  return (
    <div>
      <h2>Compromissos</h2>
      <Link to="/compromissos/novo">Novo Compromisso</Link>
      <ul>
        {compromissos.map(c => (
          <li key={c.id}>{c.nome} - {c.descricao}</li>
        ))}
      </ul>
    </div>
  )
}
