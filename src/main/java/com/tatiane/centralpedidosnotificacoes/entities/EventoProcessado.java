package com.tatiane.centralpedidosnotificacoes.entities;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoProcessado {

    private String idEventoProcessado;

}
