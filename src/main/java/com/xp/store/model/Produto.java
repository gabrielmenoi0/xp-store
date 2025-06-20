package com.xp.store.model;

import com.xp.store.dto.ProdutoDTO;
import com.xp.store.dto.ProdutoExpoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome", length = 60, nullable = false)
    private String nome;

    @Column(name = "descricao", length = 200)
    private String descricao;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;

    @Column(name = "categoria", length = 40)
    private String categoria;

    public ProdutoDTO convertToDto() {
        return new ProdutoDTO(
                this.id,
                this.nome,
                this.descricao,
                this.preco,
                this.quantidadeEstoque,
                this.categoria
        );
    }

    public ProdutoExpoDTO convertToExpoDto() {
        return new ProdutoExpoDTO(
                this.id,
                this.nome,
                this.preco,
                this.quantidadeEstoque
        );
    }
}
