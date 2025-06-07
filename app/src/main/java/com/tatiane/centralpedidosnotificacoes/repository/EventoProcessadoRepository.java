package com.tatiane.centralpedidosnotificacoes.repository;

import com.tatiane.centralpedidosnotificacoes.entities.EventoProcessado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoProcessadoRepository extends JpaRepository<EventoProcessado, String> {

    @Query(
            value = "SELECT * FROM EVENTO_PROCESSADO  u WHERE u.id_evento_processado = ?",
            nativeQuery = true)
    EventoProcessado findByIdEventoProcessado(String idEventoProcessado);

}
