import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function CreateCompromisso() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ nome: '', descricao: '', dataInicio: '' })

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = e => {
    e.preventDefault()
    fetch('/compromissos/criarcompromisso', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    }).then(() => navigate('/compromissos'))
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Novo Compromisso</h2>
      <input name="nome" placeholder="Nome" value={form.nome} onChange={handleChange} />
      <input name="descricao" placeholder="Descrição" value={form.descricao} onChange={handleChange} />
      <input type="date" name="dataInicio" value={form.dataInicio} onChange={handleChange} />
      <button type="submit">Salvar</button>
    </form>
  )
}
