package com.xp.store.model;

import com.xp.store.dto.ClienteDTO;
import com.xp.store.dto.ClienteExpoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Entity
@Table(name = "tb_cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome",length = 55, nullable = false)
    private String nome;

    @Column(name = "email",length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "senha",length = 50, nullable = false)
    private String senha;

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "telefone",length = 15)
    private String telefone;

    @Column(name = "data_nascimento",length = 20)
    private String dataNascimento;

    @Column(name = "endereco",length = 100)
    private String endereco;

    @Column(name = "cidade",length = 25)
    private String cidade;

    @Column(name = "estado",length = 15)
    private String estado;

    @Column(name = "cep",length = 15)
    private String cep;


    public ClienteDTO convertToDto() {
        return new ClienteDTO(
                this.id,
                this.nome,
                this.email,
                this.senha,
                this.cpf,
                this.telefone,
                this.dataNascimento,
                this.endereco,
                this.cidade,
                this.estado,
                this.cep
        );
    }

    public ClienteExpoDTO convertToExpoDto() {
        return new ClienteExpoDTO(
                this.id,
                this.nome,
                this.email,
                this.cpf,
                this.telefone,
                this.dataNascimento,
                this.endereco,
                this.cidade,
                this.estado,
                this.cep
        );
    }
}