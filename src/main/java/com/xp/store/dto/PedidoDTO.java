package com.xp.store.dto;

import com.xp.store.model.Cliente;
import com.xp.store.model.Pedido;
import com.xp.store.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoDTO(
        UUID id,
        Cliente cliente,
        List<UUID> produtosIds,
        LocalDateTime dataCriacao,
        String status,
        BigDecimal total
) {
    public Pedido convertToPedido( List<Produto> produtos) {
        return Pedido.builder()
                .id(this.id)
                .cliente(cliente)
                .produtos(produtos)
                .dataCriacao(this.dataCriacao)
                .status(this.status != null ? this.status : "REALIZADO")
                .total(produtos.stream()
                        .map(Produto::getPreco)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }
}