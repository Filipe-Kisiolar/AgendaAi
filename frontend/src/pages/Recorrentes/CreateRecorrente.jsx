import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function CreateRecorrente() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ nome: '', diaDaSemana: 'MONDAY' })

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = e => {
    e.preventDefault()
    fetch('/compromissosrecorrentes/criarcompromisso', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    }).then(() => navigate('/recorrentes'))
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Novo Compromisso Recorrente</h2>
      <input name="nome" placeholder="Nome" value={form.nome} onChange={handleChange} />
      <select name="diaDaSemana" value={form.diaDaSemana} onChange={handleChange}>
        <option value="MONDAY">Segunda</option>
        <option value="TUESDAY">Terça</option>
        <option value="WEDNESDAY">Quarta</option>
        <option value="THURSDAY">Quinta</option>
        <option value="FRIDAY">Sexta</option>
        <option value="SATURDAY">Sábado</option>
        <option value="SUNDAY">Domingo</option>
      </select>
      <button type="submit">Salvar</button>
    </form>
  )
}
