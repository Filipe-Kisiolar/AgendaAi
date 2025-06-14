import { Link } from 'react-router-dom'

export default function Menu() {
  return (
    <div>
      <h1>Menu Principal</h1>
      <ul>
        <li><Link to="/compromissos">Gerenciar compromissos</Link></li>
        <li><Link to="/recorrentes">Gerenciar compromissos recorrentes</Link></li>
      </ul>
    </div>
  )
}
