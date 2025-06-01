package com.tatiane.centralpedidosnotificacoes.consumers;

import com.tatiane.centralpedidosnotificacoes.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificacaoConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "pedido.criado", groupId = "notificacao-group")
    public void consumirMensagemNotificacao(String mensagem) {
        log.info("Iniciando consumo de mensagem para enviar notificacao:");
        emailService.processaConfirmacao(mensagem);
    }
}