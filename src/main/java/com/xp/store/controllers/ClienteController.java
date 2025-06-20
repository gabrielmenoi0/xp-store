package com.xp.store.controllers;

import com.xp.store.dto.ClienteDTO;
import com.xp.store.dto.ClienteExpoDTO;
import com.xp.store.service.ClienteService;
import com.xp.store.model.Cliente;
import com.xp.store.utils.Pagination.OfMeasureSpecification;
import com.xp.store.utils.Validations.Validations;
import com.xp.store.utils.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("api/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final ResponseUtils responseUtils = new ResponseUtils();

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos os clientes com paginação e filtro opcional")
    @GetMapping
    public ResponseEntity<Object> getAllClients(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "filterBy", required = false) String filterBy,
            @RequestParam(value = "filter", required = false) String filter) {

        pageSize = Math.min(pageSize, 100);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, orderBy));

        Page<Cliente> userOfMeasure;
        if (filterBy != null && filter != null) {
            userOfMeasure = clienteService.findAll(OfMeasureSpecification.withCustomFilterClient(filterBy, filter), pageable);
        } else {
            userOfMeasure = clienteService.findAll(OfMeasureSpecification.withCustomFilterClient(filterBy, filter), pageable);
        }

        userOfMeasure = clienteService.findAll(OfMeasureSpecification.withCustomFilterClient(filterBy, filter), pageable);
        List<Cliente> clienteList = userOfMeasure.get().toList();
        List<ClienteExpoDTO> clienteExpoDTOList = clienteList.stream().map(Cliente::convertToExpoDto).toList();
        Page<ClienteExpoDTO> dtoPage = new PageImpl<>(
                clienteExpoDTOList,
                userOfMeasure.getPageable(),
                userOfMeasure.getTotalElements()
        );
        return responseUtils.successResponse(HttpStatus.OK, 200, "Clientes encontrados", dtoPage);
    }


    @Operation(summary = "Criar um novo cliente")
    @PostMapping
    public ResponseEntity<Object> createClient(@RequestBody ClienteDTO clienteDTO) {
        String validationError = validateClientDTO(clienteDTO);
        if (validationError != null) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha na validação", validationError);
        }

        Optional<Cliente> existingClient = clienteService.findByEmail(clienteDTO.email());
        if (existingClient.isPresent()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Email já cadastrado", null);
        }

        Cliente cliente = clienteDTO.convertUser(null);
        Cliente saved = clienteService.save(cliente);
        return responseUtils.successResponse(HttpStatus.CREATED, 201, "Cliente criado com sucesso", saved);
    }

    @Operation(summary = "Atualizar cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClient(@RequestBody ClienteDTO clienteDTO, @RequestParam("id") UUID id) {
        String validationError = validateClientDTO(clienteDTO);
        if (validationError != null) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha na validação", validationError);
        }

        Optional<Cliente> existing = clienteService.findById(id);
        if (existing.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Cliente não encontrado", null);
        }


        if (!existing.get().getEmail().equals(clienteDTO.email()) && existing.get().getId() != id) {
            Optional<Cliente> clientWithEmail = clienteService.findByEmail(clienteDTO.email());
            if (clientWithEmail.isPresent()) {
                return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Email já cadastrado", null);
            }
        }

        Cliente clienteToUpdate = clienteDTO.convertUser(id);
        int updated = clienteService.update(id, clienteToUpdate);
        if(updated == 1){
            return responseUtils.successResponse(HttpStatus.OK, 200, "Cliente atualizado com sucesso", clienteToUpdate);
        }
        return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nâo foi possível atualizar o cliente", updated);

    }

    @Operation(summary = "Excluir cliente pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClient(@RequestParam("id") UUID id) {
        try {
            clienteService.delete(id);
            return responseUtils.successResponse(HttpStatus.OK, 200, "Cliente removido com sucesso", null);
        } catch (Exception e) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Falha ao remover cliente", e.getMessage());
        }
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@RequestParam("id") UUID id) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Cliente não encontrado", null);
        }
        return responseUtils.successResponse(HttpStatus.OK, 200, "Cliente encontrado", cliente.get().convertToExpoDto());
    }

    @Operation(summary = "Buscar clientes por nome")
    @GetMapping("/name")
    public ResponseEntity<Object> getClientsByName(@RequestParam("nome") String nome) {
        List<Cliente> clients = clienteService.findByName(nome);
        if (clients.isEmpty()) {
            return responseUtils.errorResponse(HttpStatus.BAD_REQUEST, 400, "Nenhum cliente encontrado com esse nome", null);
        }

        List<ClienteExpoDTO> clientDTOS = clients.stream().map(Cliente::convertToExpoDto).toList();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Clientes encontrados", clientDTOS);
    }

    @Operation(summary = "Contar quantidade total de clientes")
    @GetMapping("/count")
    public ResponseEntity<Object> countClients() {
        long count = clienteService.count();
        return responseUtils.successResponse(HttpStatus.OK, 200, "Total de clientes", count);
    }

    private String validateClientDTO(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return "Dados do cliente não podem ser nulos.";
        }
        if (clienteDTO.name() == null || clienteDTO.name().isBlank()) {
            return "Nome é obrigatório.";
        }
        if (!Validations.validateFullName(clienteDTO.name())) {
            return "Nome completo inválido.";
        }
        if (clienteDTO.email() == null || clienteDTO.email().isBlank()) {
            return "Email é obrigatório.";
        }
        if (!Validations.validateEmail(clienteDTO.email())) {
            return "Formato de email inválido.";
        }
        if (clienteDTO.senha() == null || clienteDTO.senha().isBlank()) {
            return "Senha é obrigatória.";
        }
        if (!Validations.validatePassword(clienteDTO.senha())) {
            return "Senha deve ter pelo menos 8 caracteres, incluir número, letra e caractere especial.";
        }
        return null;
    }
}