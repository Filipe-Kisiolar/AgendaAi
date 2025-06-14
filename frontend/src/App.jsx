import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Menu from './pages/Menu'
import ListCompromissos from './pages/Compromissos/ListCompromissos'
import CreateCompromisso from './pages/Compromissos/CreateCompromisso'
import ListRecorrentes from './pages/Recorrentes/ListRecorrentes'
import CreateRecorrente from './pages/Recorrentes/CreateRecorrente'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Menu />} />
        <Route path="/compromissos" element={<ListCompromissos />} />
        <Route path="/compromissos/novo" element={<CreateCompromisso />} />
        <Route path="/recorrentes" element={<ListRecorrentes />} />
        <Route path="/recorrentes/novo" element={<CreateRecorrente />} />
      </Routes>
    </BrowserRouter>
  )
}
