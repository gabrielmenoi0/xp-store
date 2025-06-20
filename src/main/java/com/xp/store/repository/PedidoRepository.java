package com.xp.store.repository;

import com.xp.store.model.Pedido;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE Pedido p SET
            p.cliente = :#{#pedido.cliente},
            p.produtos = :#{#pedido.produtos},
            p.dataCriacao = :#{#pedido.dataCriacao},
            p.status = :#{#pedido.status},
            p.total = :#{#pedido.total}
        WHERE p.id = :id
    """)
    int updatePedido(@Param("id") UUID id, @Param("pedido") Pedido pedido);

    List<Pedido> findByStatusIgnoreCase(String status);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteId(@Param("clienteId") UUID clienteId);

    List<Pedido> findByDataCriacaoBetween(LocalDateTime start, LocalDateTime end);
}
