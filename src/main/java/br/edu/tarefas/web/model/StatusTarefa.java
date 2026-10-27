package br.edu.tarefas.web.model;

public enum StatusTarefa {
    PENDENTE("Pendente"), EM_ANDAMENTO("Em andamento"), CONCLUIDA("Concluída");
    private final String rotulo;
    StatusTarefa(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
}
