package com.xp.store.repository;

import com.xp.store.model.Produto;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE Produto p SET
            p.nome = :#{#produto.nome},
            p.descricao = :#{#produto.descricao},
            p.preco = :#{#produto.preco},
            p.quantidadeEstoque = :#{#produto.quantidadeEstoque},
            p.categoria = :#{#produto.categoria}
        WHERE p.id = :id
    """)
    int updateProduto(@Param("id") UUID id, @Param("produto") Produto produto);

    Optional<Produto> findByNomeIgnoreCase(String nome);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaIgnoreCase(String categoria);

    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);
}