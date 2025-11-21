package com.fiap.gs.demo.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemasEnum {
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    MOBILE("Mobile"),
    DEVOPS("DevOps"),
    IA("Inteligência Artificial"),
    SEGURANCA("Segurança"),
    UX("UX"),
    UI("UI"),
    PRODUTOS("Produtos"),
    MARKETING("Marketing");


    private String label;

    public String getLabel() {
        return label;
    }
}
