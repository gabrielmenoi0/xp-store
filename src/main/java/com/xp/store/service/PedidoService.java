package com.xp.store.service;


import com.xp.store.model.Pedido;
import com.xp.store.repository.PedidoRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public int update(UUID id, Pedido pedido) {
        return pedidoRepository.updatePedido(id, pedido);
    }

    public Optional<Pedido> findById(UUID id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findAllWithoutPagination() {
        return pedidoRepository.findAll();
    }

    public Page<Pedido> findAll(Specification<Pedido> spec, Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    public List<Pedido> findByStatus(String status) {
        return pedidoRepository.findByStatusIgnoreCase(status);
    }

    public List<Pedido> findByClienteId(UUID clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> findByDataCriacaoBetween(LocalDateTime start, LocalDateTime end) {
        return pedidoRepository.findByDataCriacaoBetween(start, end);
    }

    public void delete(UUID id) {
        pedidoRepository.deleteById(id);
    }

    public long count() {
        return pedidoRepository.count();
    }
}
