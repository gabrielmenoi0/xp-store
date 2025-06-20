package com.xp.store.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoExpoDTO(
        UUID id,
        String nome,
        BigDecimal preco,
        Integer quantidadeEstoque
) {}