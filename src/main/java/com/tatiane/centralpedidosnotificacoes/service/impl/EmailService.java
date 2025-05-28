package com.tatiane.centralpedidosnotificacoes.service.impl;

import com.tatiane.centralpedidosnotificacoes.dto.Evento;
import com.tatiane.centralpedidosnotificacoes.dto.Pedido;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tatiane.centralpedidosnotificacoes.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificacaoService {

    private final JavaMailSender mailSender;

    private final ObjectMapper objectMapper;

    @Value("${notificacao.email.pedido_confirmado.assunto}")
    private String assuntoEmailConfirmado;

    @Value("${notificacao.email.pedido_confirmado.texto}")
    private String textoEmailConfirmado;

    public void enviaConfirmacao(Evento evento) {
        try {
            log.info("Extraindo mensagem do json recebido: {}", evento.getMensagemPedido());
            Pedido pedido = objectMapper.readValue(evento.getMensagemPedido(), Pedido.class);

            log.info("Enviando mensagem para o cliente: {}, id do pedido: {}", pedido.getNomeCliente(), evento.getMensagemPedido());
            this.enviarMensagem(pedido.getEmailCliente(), pedido.getIdPedido());

        } catch (JsonProcessingException e) {
            log.error("Erro ao desserializar evento: {}", evento.getMensagemPedido(), e);
            throw new RuntimeException("Erro ao processar evento de pedido", e);
        }
    }

    public void enviarMensagem(String destinatarioEmail, Long idPedido) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatarioEmail);
        mensagem.setSubject(assuntoEmailConfirmado);
        mensagem.setText(textoEmailConfirmado + " " + idPedido);
        mailSender.send(mensagem);
    }
}
