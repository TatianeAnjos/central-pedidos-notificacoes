package com.tatiane.centralpedidosnotificacoes.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoProcessado {
    @Id
    private String idEventoProcessado;

}
