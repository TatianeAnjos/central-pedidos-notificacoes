# Central Pedidos Notificações -Projeto criado para fins de estudo

## Módulo: Mensageria com Resiliência
Projeto com foco em mensageria resiliente. Responsável por consumir as mensagem do tópico kafka produzidos pelo projeto central-pedidos-assincrona.
Cada mensagem consumida contém dados do cliente como nome, id do pedido, e-mail e telefone, além de conter o id da mensagem.

## Responsabilidade: 
Consumir mensagem do tópico kafka e fazer o envio das notificações ao cliente;
Atualmente faz o envio do e-mail do pedido realizado;

## Objetivo:
Garantir o envio de notificações por e-mail com:
Resiliência a falhas temporárias
Tolerância a falhas persistentes
Idempotência para evitar retrabalho

## Melhorias futuras:
Envio de sms de pedido realizado;
Envio de notificações para demais operações da central de pedidos;

## Tecnologias e dependências
Spring Boot 3
Java Mail (JavaMailSender)
Resilience4j (retry, circuitbreaker)
Spring Retry (Realiza nova tentativa em caso de falha)
Circuit breaker (Faz o break de uma mensagem que esteja com falha)
Jackson (desserialização do evento JSON)
JPA (grava na tabela os eventos já processados)
