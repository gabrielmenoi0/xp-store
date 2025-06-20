package com.xp.store.controllers;

import com.xp.store.dto.PedidoDTO;
import com.xp.store.dto.PedidoExpoDTO;
import com.xp.store.dto.RecivePedidoDTO;
import com.xp.store.model.Cliente;
import com.xp.store.model.Pedido;
import com.xp.store.model.Produto;
import com.xp.store.service.ClienteService;
import com.xp.store.service.PedidoService;
import com.xp.store.service.ProdutoService;

import com.xp.store.utils.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final ResponseUtils responseUtils = new ResponseUtils();

    public PedidoController(PedidoService pedidoService, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    @Operation(summary = "Listar todos os pedidos com paginação")
    @GetMapping
    public ResponseEntity<Object> getAllPedidos(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "dataCriacao") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, orderBy));

        Page<Pedido> page = pedidoService.findAll(null, pageable);
        List<PedidoExpoDTO> expos = page.stream().map(Pedido::convertToExpoDto).toList();

        return responseUtils.successResponse(HttpStatus.OK, 200, "Pedidos encontrados",
                new PageImpl<>(expos, pageable, page.getTotalElements()));
    }

    @Operation(summary = "Criar um novo pedido")
    @PostMapping
    public ResponseEntity<Object> createPedido(@RequestBody RecivePedidoDTO dto) {
        Optional<Cliente> clienteOpt = clienteService.findById(dto.idCliente());
        if (clienteOpt.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Cliente não encontrado", null);
        }

        List<Produto> produtos = dto.produtos().stream()
                .map(produtoService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (produtos.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum produto válido fornecido", null);
        }

        Pedido pedido = dto.convertToPedido(clienteOpt.get(),produtos);
        Pedido saved = pedidoService.save(pedido);
        return responseUtils.successResponse(HttpStatus.CREATED, 201, "Pedido criado com sucesso", saved.convertToExpoDto());
    }

    @Operation(summary = "Atualizar um pedido existente")
    @PutMapping("/id")
    public ResponseEntity<Object> updatePedido(@RequestParam("id") UUID id, @RequestBody RecivePedidoDTO dto) {
        Optional<Pedido> existing = pedidoService.findById(id);
        if (existing.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Pedido não encontrado", null);
        }

        Optional<Cliente> clienteOpt = clienteService.findById(dto.idCliente());
        if (clienteOpt.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Cliente não encontrado", null);
        }

        List<Produto> produtos = dto.produtos().stream()
                .map(produtoService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (produtos.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum produto válido fornecido", null);
        }

        Pedido toUpdate = dto.convertToPedido(clienteOpt.get(), produtos);
        int updated = pedidoService.update(id, toUpdate);
        if (updated == 1) {
            return responseUtils.successResponse(HttpStatus.OK, 200, "Pedido atualizado com sucesso", toUpdate.convertToExpoDto());
        }
        return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Não foi possível atualizar o pedido", updated);
    }

    @Operation(summary = "Excluir pedido pelo ID")
    @DeleteMapping("/id")
    public ResponseEntity<Object> deletePedido(@RequestParam("id") UUID id) {
        try {
            pedidoService.delete(id);
            return responseUtils.successResponse(HttpStatus.OK, 200, "Pedido removido com sucesso", null);
        } catch (Exception e) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha ao remover pedido", e.getMessage());
        }
    }

    @Operation(summary = "Buscar pedido por ID")
    @GetMapping("/id")
    public ResponseEntity<Object> getPedidoById(@RequestParam("id") UUID id) {
        Optional<Pedido> pedido = pedidoService.findById(id);
        return pedido.map(value -> responseUtils.successResponse(HttpStatus.OK, 200, "Pedido encontrado", value.convertToExpoDto()))
                .orElseGet(() -> responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Pedido não encontrado", null));
    }

    @Operation(summary = "Buscar pedidos por cliente")
    @GetMapping("/cliente/id")
    public ResponseEntity<Object> getPedidosByCliente(@RequestParam("id") UUID id) {
        List<Pedido> pedidos = pedidoService.findByClienteId(id);
        if (pedidos.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum pedido encontrado para o cliente", null);
        }
        List<PedidoExpoDTO> expos = pedidos.stream().map(Pedido::convertToExpoDto).toList();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Pedidos encontrados", expos);
    }

    @Operation(summary = "Buscar pedidos por status")
    @GetMapping("/status")
    public ResponseEntity<Object> getPedidosByStatus(@RequestParam("status") String status) {
        List<Pedido> pedidos = pedidoService.findByStatus(status);
        if (pedidos.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum pedido encontrado com esse status", null);
        }
        List<PedidoExpoDTO> expos = pedidos.stream().map(Pedido::convertToExpoDto).toList();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Pedidos encontrados", expos);
    }

    @Operation(summary = "Contar quantidade total de pedidos")
    @GetMapping("/count")
    public ResponseEntity<Object> countPedidos() {
        long count = pedidoService.count();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Total de pedidos", count);
    }
}
