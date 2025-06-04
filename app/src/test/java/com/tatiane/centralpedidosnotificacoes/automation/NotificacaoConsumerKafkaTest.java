package com.tatiane.centralpedidosnotificacoes.automation;

import com.tatiane.centralpedidosnotificacoes.entities.EventoProcessado;
import com.tatiane.centralpedidosnotificacoes.repository.EventoProcessadoRepository;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@EmbeddedKafka(partitions = 1, topics = {"pedido.criado"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest
class NotificacaoConsumerKafkaTest {

    @Autowired
    private EventoProcessadoRepository eventoProcessadoRepository;

    private KafkaTemplate<String, String> criarKafkaTemplate() {
        Map<String, Object> configs = KafkaTestUtils.producerProps("localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configs);
        return new KafkaTemplate<>(producerFactory);
    }

    @Test
    void deveProcessarMensagemDeConfirmacaoComSucesso() throws Exception {
        // Arrange
        KafkaTemplate<String, String> kafkaTemplate = criarKafkaTemplate();
        String mensagemJson = """
                {"mensagemPedido":"{\\"idPedido\\":4,\\"nomeCliente\\":\\"Cliente 1\\",\\"emailCliente\\":\\"teste@gmail\\",\\"listaProdutos\\":[{\\"id\\":5,\\"idProduto\\":125,\\"nomeProduto\\":\\"Televisao\\",\\"quantidadeProduto\\":1}]}","idMensagem":"1245"}
                """;
        // Act
        CompletableFuture<SendResult<String, String>> a = kafkaTemplate.send("pedido.criado", mensagemJson);
        Thread.sleep(2000); // aguarda listener processar

        // Assert
        EventoProcessado eventoSalvo =  eventoProcessadoRepository.findByIdEventoProcessado("1245");

        Assertions.assertNotNull(eventoSalvo);
    }
}
