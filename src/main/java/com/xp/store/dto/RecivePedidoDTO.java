package com.xp.store.dto;

import com.xp.store.model.Cliente;
import com.xp.store.model.Pedido;
import com.xp.store.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RecivePedidoDTO (
            UUID idCliente,
            List<UUID> produtos
) {
    public Pedido convertToPedido(Cliente cliente, List<Produto> produtosEntity) {
        BigDecimal total = produtosEntity.stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Pedido.builder()
                .cliente(cliente)
                .produtos(produtosEntity)
                .dataCriacao(LocalDateTime.now())
                .status("ABERTO")
                .total(total)
                .build();
    }
}