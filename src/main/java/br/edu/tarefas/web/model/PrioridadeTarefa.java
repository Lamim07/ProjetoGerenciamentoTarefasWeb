package br.edu.tarefas.web.model;

public enum PrioridadeTarefa {
    BAIXA("Baixa"), MEDIA("Média"), ALTA("Alta");
    private final String rotulo;
    PrioridadeTarefa(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }
}
