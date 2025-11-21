package com.fiap.gs.demo.topicos.enums;

public enum NivelTopicoEnum {
    BASICO("Básico"),
    INTERMEDIARIO("Intermediário"),
    AVANCADO("Avançado");

    private String label;

    NivelTopicoEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
