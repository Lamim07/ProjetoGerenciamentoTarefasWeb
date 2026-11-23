document.querySelectorAll('form[data-confirm]').forEach(form => {
  form.addEventListener('submit', event => {
    if (!window.confirm(form.dataset.confirm)) event.preventDefault();
  });
});

const formularioIa = document.querySelector('[data-assistente-form]');
if (formularioIa) {
  const botoesIa = [...formularioIa.querySelectorAll('[data-ia-acao]')];
  const statusIa = formularioIa.querySelector('[data-ia-status]');
  const titulo = formularioIa.querySelector('[name="titulo"]');
  const descricao = formularioIa.querySelector('[name="descricao"]');
  const prazo = formularioIa.querySelector('[name="prazo"]');
  const prioridade = formularioIa.querySelector('[name="prioridade"]');

  botoesIa.forEach(botao => botao.addEventListener('click', async () => {
    const acao = botao.dataset.iaAcao;
    if (!titulo.value.trim()) {
      statusIa.textContent = 'Informe o título antes de consultar o Gemini.';
      titulo.focus();
      return;
    }
    if (acao === 'prioridade' && !prazo.value) {
      statusIa.textContent = 'Informe o prazo antes de sugerir a prioridade.';
      prazo.focus();
      return;
    }
    if (acao === 'descricao' && descricao.value.trim() &&
        !window.confirm('Substituir a descrição atual pela sugestão do Gemini?')) return;

    botoesIa.forEach(item => item.disabled = true);
    statusIa.textContent = 'Consultando o Gemini...';
    try {
      const resposta = await fetch(botao.dataset.url, {
        method: 'POST', credentials: 'same-origin',
        headers: { 'Accept': 'application/json' },
        body: new FormData(formularioIa)
      });
      const dados = await resposta.json();
      if (!resposta.ok) throw new Error(dados.erro || 'Não foi possível gerar a sugestão.');
      if (acao === 'descricao') {
        descricao.value = dados.valor;
        descricao.focus();
        statusIa.textContent = 'Descrição preenchida. Revise o texto antes de salvar.';
      } else {
        prioridade.value = dados.valor;
        prioridade.focus();
        statusIa.textContent = 'Prioridade selecionada. Confirme se faz sentido antes de salvar.';
      }
    } catch (erro) {
      statusIa.textContent = erro.message || 'Não foi possível consultar o Gemini agora.';
    } finally {
      botoesIa.forEach(item => item.disabled = false);
    }
  }));
}
