package com.xp.store.controllers;

import com.xp.store.dto.ProdutoDTO;
import com.xp.store.dto.ProdutoExpoDTO;
import com.xp.store.model.Produto;
import com.xp.store.service.ProdutoService;
import com.xp.store.utils.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.xp.store.utils.Pagination.OfMeasureSpecification;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("api/v1/produto")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ResponseUtils responseUtils = new ResponseUtils();

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Listar todos os produtos com paginação e filtro opcional")
    @GetMapping
    public ResponseEntity<Object> getAllProdutos(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "filterBy", required = false) String filterBy,
            @RequestParam(value = "filter", required = false) String filter) {

        pageSize = Math.min(pageSize, 100);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, orderBy));

        Page<Produto> produtoPage = produtoService.findAll(OfMeasureSpecification.withCustomFilterProduto(filterBy, filter), pageable);
        List<ProdutoExpoDTO> produtoDTOs = produtoPage.stream().map(Produto::convertToExpoDto).toList();

        Page<ProdutoExpoDTO> dtoPage = new PageImpl<>(produtoDTOs, produtoPage.getPageable(), produtoPage.getTotalElements());
        return responseUtils.successResponse(HttpStatus.OK, 200, "Produtos encontrados", dtoPage);
    }

    @Operation(summary = "Criar um novo produto")
    @PostMapping
    public ResponseEntity<Object> createProduto(@RequestBody ProdutoDTO produtoDTO) {
        String validationError = validateProdutoDTO(produtoDTO);
        if (validationError != null) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha na validação", validationError);
        }

        Produto produto = produtoDTO.convertEntity(null);
        Produto saved = produtoService.save(produto);
        return responseUtils.successResponse(HttpStatus.CREATED, 201, "Produto criado com sucesso", saved);
    }

    @Operation(summary = "Atualizar produto existente")
    @PutMapping("/id")
    public ResponseEntity<Object> updateProduto(@RequestBody ProdutoDTO produtoDTO, @RequestParam("id") UUID id) {
        String validationError = validateProdutoDTO(produtoDTO);
        if (validationError != null) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha na validação", validationError);
        }

        Optional<Produto> existing = produtoService.findById(id);
        if (existing.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Produto não encontrado", null);
        }

        Produto produtoToUpdate = produtoDTO.convertEntity(id);
        int updated = produtoService.update(id, produtoToUpdate);
        if (updated == 1) {
            return responseUtils.successResponse(HttpStatus.OK, 200, "Produto atualizado com sucesso", produtoToUpdate);
        }
        return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Não foi possível atualizar o produto", updated);
    }

    @Operation(summary = "Excluir produto pelo ID")
    @DeleteMapping("/id")
    public ResponseEntity<Object> deleteProduto(@RequestParam("id") UUID id) {
        try {
            produtoService.delete(id);
            return responseUtils.successResponse(HttpStatus.OK, 200, "Produto removido com sucesso", null);
        } catch (Exception e) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha ao remover produto", e.getMessage());
        }
    }

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/id")
    public ResponseEntity<Object> getProdutoById(@RequestParam("id") UUID id) {
        Optional<Produto> produto = produtoService.findById(id);
        if (produto.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Produto não encontrado", null);
        }
        return responseUtils.successResponse(HttpStatus.OK, 200, "Produto encontrado", produto.get().convertToExpoDto());
    }

    @Operation(summary = "Buscar produtos por nome")
    @GetMapping("/nome")
    public ResponseEntity<Object> getProdutoByNome(@RequestParam("nome") String nome) {
        List<Produto> produtos = produtoService.findByNome(nome);
        if (produtos.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum produto encontrado com esse nome", null);
        }

        List<ProdutoExpoDTO> produtoDTOs = produtos.stream().map(Produto::convertToExpoDto).toList();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Produtos encontrados", produtoDTOs);
    }

    @Operation(summary = "Contar quantidade total de produtos")
    @GetMapping("/count")
    public ResponseEntity<Object> countProdutos() {
        long count = produtoService.count();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Total de produtos", count);
    }

    private String validateProdutoDTO(ProdutoDTO produtoDTO) {
        if (produtoDTO == null) return "Dados do produto não podem ser nulos.";
        if (produtoDTO.nome() == null || produtoDTO.nome().isBlank()) return "Nome é obrigatório.";
        if (produtoDTO.preco() == null || produtoDTO.preco().compareTo(BigDecimal.ZERO) <= 0) return "Preço deve ser maior que zero.";
        if (produtoDTO.quantidadeEstoque() == null || produtoDTO.quantidadeEstoque() < 0) return "Quantidade em estoque não pode ser negativa.";
        return null;
    }
}