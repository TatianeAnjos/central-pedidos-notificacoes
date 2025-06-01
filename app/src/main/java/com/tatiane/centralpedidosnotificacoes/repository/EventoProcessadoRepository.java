package com.tatiane.centralpedidosnotificacoes.repository;

import com.tatiane.centralpedidosnotificacoes.entities.EventoProcessado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoProcessadoRepository extends JpaRepository<EventoProcessado, String> {

}
