package com.tatiane.centralpedidosnotificacoes.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tatiane.centralpedidosnotificacoes.dto.Evento;
import com.tatiane.centralpedidosnotificacoes.dto.Pedido;
import com.tatiane.centralpedidosnotificacoes.entities.EventoProcessado;
import com.tatiane.centralpedidosnotificacoes.repository.EventoProcessadoRepository;
import com.tatiane.centralpedidosnotificacoes.service.NotificacaoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificacaoService {

    private final JavaMailSender mailSender;

    private final ObjectMapper objectMapper;

    private final EventoProcessadoRepository eventoProcessadoRepository;


    @Value("${notificacao.email.pedido_confirmado.assunto}")
    private String assuntoEmailConfirmado;

    @Value("${notificacao.email.pedido_confirmado.texto}")
    private String textoEmailConfirmado;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(name = "emailService", fallbackMethod = "fallbackEnvioEmail")
    public void processaConfirmacao(Evento evento) {
        Optional<EventoProcessado> eventoProcessado = eventoProcessadoRepository.findById(evento.getIdMensagem());

        if (eventoProcessado.isPresent()) {
            log.warn("Mensagem com ID {} já foi processada. Ignorando reprocessamento.", evento.getIdMensagem());
            return;
        }

        Pedido pedido = this.extrairPedido(evento);

        log.info("Enviando mensagem para o cliente: {}, id do pedido: {}", pedido.getNomeCliente(), evento.getMensagemPedido());
        this.enviarMensagem(pedido.getEmailCliente(), pedido.getIdPedido());

        log.info("Salvando id de mensagem processada no banco de dados. id da mensagem: {} ", evento.getIdMensagem());
        eventoProcessadoRepository.save(EventoProcessado.builder().idEventoProcessado(evento.getIdMensagem()).build());

        log.info("Confirmação enviada com sucesso");
    }

    private Pedido extrairPedido(Evento evento) {
        try {
            log.info("Extraindo mensagem do json recebido: {}", evento.getMensagemPedido());
            return objectMapper.readValue(evento.getMensagemPedido(), Pedido.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao desserializar evento de pedido", e);
        }
    }

    public void enviarMensagem(String destinatarioEmail, Long idPedido) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatarioEmail);
        mensagem.setSubject(assuntoEmailConfirmado);
        mensagem.setText(textoEmailConfirmado + " " + idPedido);
        mailSender.send(mensagem);
    }

    @Recover
    public void recover(Evento evento) {
        Pedido pedido = this.extrairPedido(evento);

        log.error("Falha ao enviar e-mail após várias tentativas para {}: idPedido {}",
                pedido.getEmailCliente(),
                pedido.getIdPedido());
    }

    public void fallbackEnvioEmail(Evento evento, Exception ex) {
        Pedido pedido = this.extrairPedido(evento);

        log.error("CIRCUIT BREAKER ATIVO - Falha ao enviar e-mail para {} (idPedido {}): {}",
                pedido.getEmailCliente(), pedido.getIdPedido(), ex.getMessage());
    }
}
