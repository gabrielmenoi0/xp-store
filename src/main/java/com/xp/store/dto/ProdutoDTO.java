package com.xp.store.dto;

import com.xp.store.model.Produto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoDTO(
        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidadeEstoque,
        String categoria
) {
    public Produto convertEntity(UUID id) {
        return Produto.builder()
                .id(id)
                .nome(this.nome)
                .descricao(this.descricao)
                .preco(this.preco)
                .quantidadeEstoque(this.quantidadeEstoque)
                .categoria(this.categoria)
                .build();
    }
}
