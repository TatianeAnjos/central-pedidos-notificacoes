package com.tatiane.centralpedidosnotificacoes.service;

import com.tatiane.centralpedidosnotificacoes.dto.Evento;

public interface NotificacaoService {

    void processaConfirmacao(Evento evento);
}
