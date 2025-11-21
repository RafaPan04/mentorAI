package com.fiap.gs.demo.exceptions.topico;

import com.fiap.gs.demo.exceptions.NotFoundException;

public class TopicoNotFoundException extends NotFoundException  {
    public TopicoNotFoundException(String message) {
        super(message);
    }
}
