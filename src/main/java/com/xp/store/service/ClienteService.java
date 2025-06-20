package com.xp.store.service;

import com.xp.store.model.Cliente;
import com.xp.store.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Page<Cliente> findAll(Specification<Cliente> clienteSpecification, Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public List<Cliente> findAllWithoutPagination() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(UUID id) {
        return Optional.ofNullable(clienteRepository.findById(id).orElse(null));
    }

    public List<Cliente> findByName(String name) {
        return clienteRepository.findByNomeContainingIgnoreCase(name);
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public int update(UUID id, Cliente updatedCliente) {
        return clienteRepository.updateCliente(id,updatedCliente);
    }

    public void delete(UUID id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> getClientByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public long count() {
        return clienteRepository.count();
    }
}
