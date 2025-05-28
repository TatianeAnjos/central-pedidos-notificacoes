package com.tatiane.centralpedidosnotificacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class CentralPedidosNotificacoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CentralPedidosNotificacoesApplication.class, args);
    }
}