package com.xp.store.service;

import com.xp.store.model.Produto;
import com.xp.store.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Page<Produto> findAll(Specification<Produto> produtoSpec, Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public List<Produto> findAllWithoutPagination() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> findById(UUID id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<Produto> findByFaixaDePreco(BigDecimal min, BigDecimal max) {
        return produtoRepository.findByPrecoBetween(min, max);
    }

    public int update(UUID id, Produto updatedProduto) {
        return produtoRepository.updateProduto(id, updatedProduto);
    }

    public void delete(UUID id) {
        produtoRepository.deleteById(id);
    }

    public long count() {
        return produtoRepository.count();
    }
}
