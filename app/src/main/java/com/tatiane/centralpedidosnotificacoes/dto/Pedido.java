package com.tatiane.centralpedidosnotificacoes.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Pedido {

    private String nomeCliente;

    private String emailCliente;

    private Long idPedido;

    private List<Produto> listaProdutoResponse;
}