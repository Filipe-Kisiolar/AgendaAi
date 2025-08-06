package kisiolar.filipe.Viviane.Ai.IntegrationAI;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class Instrucoes {

    @Tool(name = "instrucoes_criar_compromisso",
            description = "Mostra um exemplo de JSON válido para criar um compromisso. Use essa função apenas se não souber como preencher o JSON para a tool 'criar_compromisso'.")
    public String instrucoesCriarCompromisso() {
        return """
    Para criar um compromisso, envie um JSON no seguinte formato:

    {
        "nome": "Consulta médica",
        "descricao": "Retorno com o cardiologista",
        "local": "Clínica São João",
        "inicio": "2025-08-10T10:00:00",
        "fim": "2025-08-10T11:00:00",
        "compromissoRecorrenteId": null
    }

    Campos obrigatórios:
    - nome
    - descricao
    - inicio
    - fim

    Campo opcional:
    - local (pode ser uma string vazia ou null)
    - compromissoRecorrenteId (normalmente null, exceto se estiver atrelado a um compromisso recorrente)
    """;
    }

    @Tool(name = "instrucoes_editar_compromisso",
            description = "Mostra um exemplo de JSON para atualizar um compromisso. Todos os campos são opcionais e devem ser enviados como null se não forem alterados.")
    public String instrucoesEditarCompromisso() {
        return """
    Para editar um compromisso, envie um JSON no seguinte formato:

    {
        "nome": "Novo nome do compromisso",
        "descricao": "Nova descrição",
        "local": "Novo local",
        "inicio": "2025-08-10T09:30:00",
        "fim": "2025-08-10T10:30:00"
    }

    Todos os campos são opcionais.
    Se você não quiser alterar algum campo, envie com valor `null`, por exemplo:

    {
        "nome": null,
        "descricao": "Descrição atualizada",
        "local": null,
        "inicio": null,
        "fim": null
    }

    Lembre-se: você precisa informar o ID do compromisso no momento de chamar a tool 'alterar_compromisso',
    você pode descobri-lo ao buscar o compromisso pelo nome na tool que faz essa ação.
    """;
    }
}
