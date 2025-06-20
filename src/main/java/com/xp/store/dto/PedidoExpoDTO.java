package com.xp.store.dto;

import com.xp.store.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoExpoDTO(
        UUID id,
        UUID idCliente,
        List<Produto> produtos,
        LocalDateTime dataCriacao,
        String status,
        BigDecimal total
) {}