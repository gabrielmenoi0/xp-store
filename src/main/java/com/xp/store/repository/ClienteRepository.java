package com.xp.store.repository;

import com.xp.store.model.Cliente;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    @Modifying
    @Transactional
    @Query("""
    UPDATE Cliente c SET
        c.nome = :#{#cliente.nome},
        c.email = :#{#cliente.email},
        c.senha = :#{#cliente.senha},
        c.cpf = :#{#cliente.cpf},
        c.telefone = :#{#cliente.telefone},
        c.dataNascimento = :#{#cliente.dataNascimento},
        c.endereco = :#{#cliente.endereco},
        c.cidade = :#{#cliente.cidade},
        c.estado = :#{#cliente.estado},
        c.cep = :#{#cliente.cep}
    WHERE c.id = :id
""")
    int updateCliente(@Param("id") UUID id, @Param("cliente") Cliente cliente);


    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> findByEmail(@Param("email") String email);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

}
